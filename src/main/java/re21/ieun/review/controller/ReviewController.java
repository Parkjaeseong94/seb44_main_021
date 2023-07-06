package re21.ieun.review.controller;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import re21.ieun.member.dto.MemberDto;
import re21.ieun.member.service.MemberService;
import re21.ieun.review.dto.ReviewPatchDto;
import re21.ieun.review.dto.ReviewPostDto;
import re21.ieun.review.dto.ReviewResponseDto;
import re21.ieun.review.entity.Review;
import re21.ieun.review.mapper.ReviewMapper;
import re21.ieun.review.service.ReviewService;
import re21.ieun.utils.UriCreator;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Validated
public class ReviewController {

    private final static String ANSWER_DEFAULT_URL = "/reviews";

    private final ReviewService reviewService;

    private final ReviewMapper reviewMapper;

    private final MemberService memberService;

    public ReviewController(ReviewService reviewService, ReviewMapper reviewMapper, MemberService memberService) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
        this.memberService = memberService;
    }

    @PostMapping
    public ResponseEntity<?> postReview(@Valid @RequestBody ReviewPostDto reviewPostDto) {

        memberService.findMember(reviewPostDto.getMemberId());

        Review review = reviewService.createReview(reviewMapper.reviewPostDtoToReview(reviewPostDto));
        URI location = UriCreator.createUri(ANSWER_DEFAULT_URL, review.getReviewId());

        return ResponseEntity.created(location).build();
    }

    // 리뷰 수정

    // 리뷰 삭제

    @GetMapping("/{review-id}")
    public ResponseEntity<?> getReview(@PathVariable("review-id") @Positive long reviewId) {

        Review review = reviewService.findVeryfiReview(reviewId);

        return new ResponseEntity<>(reviewMapper.reviewToReviewResponseDto(review), HttpStatus.OK);

    }

    @GetMapping
    public ResponseEntity<?> getReviews() {

        List<ReviewResponseDto> reviews = reviewService.findReviews();

        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }
}
