package re21.ieun.auth.filter;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import re21.ieun.auth.jwt.JwtTokenizer;
import re21.ieun.auth.utils.CustomAuthorityUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JwtVerificationFilter extends OncePerRequestFilter {

    private final JwtTokenizer jwtTokenizer;
    private final CustomAuthorityUtils authorityUtils;
    private final AuthenticationManager authenticationManager;

    public JwtVerificationFilter(JwtTokenizer jwtTokenizer,
                                 CustomAuthorityUtils authorityUtils,
                                 AuthenticationManager authenticationManager) {
        this.jwtTokenizer = jwtTokenizer;
        this.authorityUtils = authorityUtils;
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = request.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            String token = authorization.substring(7); // Remove "Bearer " prefix

            try {
                Map<String, Object> claims = verifyJws(token);
                setAuthenticationToContext(claims);
            } catch (SignatureException se) {
                request.setAttribute("exception", se);
            } catch (ExpiredJwtException ee) {
                handleExpiredToken(request, response);
                return;
            } catch (Exception e) {
                request.setAttribute("exception", e);
            }
        }

        filterChain.doFilter(request, response);
    }

    private Map<String, Object> verifyJws(String token) {
        String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
        Map<String, Object> claims = jwtTokenizer.getClaims(token, base64EncodedSecretKey).getBody();

        return claims;
    }

    private void setAuthenticationToContext(Map<String, Object> claims) {
        String username = (String) claims.get("username");
        List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) claims.get("roles"));
        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private void handleExpiredToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String refreshToken = request.getHeader("Refresh");

        if (refreshToken != null) {
            try {
                // Validate the refresh token
                Map<String, Object> refreshClaims = verifyJws(refreshToken);
                String username = (String) refreshClaims.get("username");
                List<GrantedAuthority> authorities = authorityUtils.createAuthorities((List) refreshClaims.get("roles"));
                Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
                Authentication refreshedAuthentication = authenticationManager.authenticate(authentication);

                // Generate new access token
                Map<String, Object> claims = new HashMap<>();
                claims.put("username", username);
                claims.put("roles", refreshClaims.get("roles"));
                String base64EncodedSecretKey = jwtTokenizer.encodeBase64SecretKey(jwtTokenizer.getSecretKey());
                String newAccessToken = jwtTokenizer.generateAccessToken(claims, username, base64EncodedSecretKey);

                response.setHeader("Authorization", "Bearer " + newAccessToken);
                response.setHeader("Refresh", refreshToken);
                response.setHeader("MemberId", String.valueOf(refreshClaims.get("memberId")));

                SecurityContextHolder.getContext().setAuthentication(refreshedAuthentication);
                return;
            } catch (ExpiredJwtException e) {
                // Refresh token is also expired, send 401 Unauthorized
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired.");
                return;
            } catch (Exception e) {
                // Other exceptions, send 500 Internal Server Error
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Internal Server Error");
                return;
            }
        }
        // No refresh token provided or refresh token is invalid, send 401 Unauthorized
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token has expired.");
    }
}