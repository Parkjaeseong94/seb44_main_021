package re21.ieun.review.mapper;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import re21.ieun.review.dto.ReviewPatchDto;
import re21.ieun.review.dto.ReviewPostDto;
import re21.ieun.review.dto.ReviewResponseDto;
import re21.ieun.review.entity.Review;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ReviewMapper {


    Review reviewPostDtoToReview(ReviewPostDto reviewPostDto);

    Review reviewPatchDtoToReview(ReviewPatchDto reviewPatchDto);

    Review reviewToReviewResponseDto(Review review);

    List<ReviewResponseDto> reveiewToReviewResponseDtos(List<Review> reviews);
}
