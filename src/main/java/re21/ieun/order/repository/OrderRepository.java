package re21.ieun.order.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import re21.ieun.order.entity.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
