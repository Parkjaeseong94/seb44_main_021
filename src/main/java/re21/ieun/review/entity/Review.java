package re21.ieun.review.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import re21.ieun.member.entity.Member;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewId;

    @Column(length = 100, nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    public enum ReviewStatus {

        REVIEW_REGISTRATION("리뷰 등록"),

        REVIEW_DELETE("리뷰 삭제");

        @Getter
        private final String status;

        ReviewStatus(String status) {this.status = status;}
    }

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReviewStatus reviewStatus = ReviewStatus.REVIEW_REGISTRATION;
}
