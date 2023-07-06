package re21.ieun.review.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re21.ieun.review.entity.Review;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository <Review, Long> {

}
