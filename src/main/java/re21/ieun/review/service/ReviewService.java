package re21.ieun.review.service;


import org.springframework.stereotype.Service;
import re21.ieun.exception.BusinessLogicException;
import re21.ieun.exception.ExceptionCode;
import re21.ieun.member.entity.Member;
import re21.ieun.member.service.MemberService;
import re21.ieun.review.dto.ReviewResponseDto;
import re21.ieun.review.entity.Review;
import re21.ieun.review.mapper.ReviewMapper;
import re21.ieun.review.repository.ReviewRepository;
import re21.ieun.sell.entity.Sell;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final ReviewMapper reviewMapper;

    private final MemberService memberService;

    public ReviewService(ReviewRepository reviewRepository, ReviewMapper reviewMapper, MemberService memberService) {
        this.reviewRepository = reviewRepository;
        this.reviewMapper = reviewMapper;
        this.memberService = memberService;
    }



    public Review createReview(Review review) {
        if (review.getMember() == null) {
            throw new IllegalArgumentException("리뷰의 회원 정보가 필요합니다.");
        }

        verifyReview(review);

        return reviewRepository.save(review);
    }

    public Review findVeryfiReview(Long reviewId) {
        if (reviewId == null) {
            throw new IllegalArgumentException("리뷰 ID가 필요합니다.");
        }
        Optional<Review> optionalReview = reviewRepository.findById(reviewId);
        Review findReview = optionalReview.orElseThrow(() ->
                new BusinessLogicException(ExceptionCode.REVIEW_NOT_FOUND));

        return findReview;
    }




    // 리뷰 수정

    public Review updateReview(Review review) {
        Review findReview = findVeryfiReview(review.getReviewId());

        Optional.ofNullable(review.getTitle())
                .ifPresent(title -> findReview.setTitle(title));

        Optional.ofNullable(review.getContent())
                .ifPresent(content -> findReview.setContent(content));

        return reviewRepository.save(findReview);
    }



    // 리뷰 삭제

    public Review deleteReview(long reviewId) {

        Review findReview = findVeryfiReview(reviewId);

        findReview.setReviewStatus(Review.ReviewStatus.REVIEW_DELETE);

        return reviewRepository.save(findReview);
    }

    // member가 존재하는지 확인
    public void verifyReview(Review review) {

        Member member = memberService.findMember(review.getMember().getMemberId());
        review.setMember(member);
    }



    // 모든 Reivew 확인
    public List<ReviewResponseDto> findReviews() {
        List<Review> reviews = reviewRepository.findAll();

        return reviewMapper.reveiewToReviewResponseDtos(reviews);
    }
}
