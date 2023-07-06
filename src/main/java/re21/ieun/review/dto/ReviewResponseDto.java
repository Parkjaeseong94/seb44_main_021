package re21.ieun.review.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Positive;

@Getter
@Setter
@NoArgsConstructor

public class ReviewResponseDto {

    @Positive
    private long memberId;
    private long reviewId;

    private String displayName;

    private String title;

    private String content;
}
