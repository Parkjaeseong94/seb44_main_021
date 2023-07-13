package re21.ieun.order.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import re21.ieun.audit.Auditable;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long OrderId;

    @Column(nullable = false)
    private String orderName;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String quantity;


    @Getter
    public enum OrderStatus {
        ORDER_RECEPTION("주문 접수"),
        ORDER_SHIPPING("배송 중"),
        ORDER_COMPLETED("배송 완료"),
        ORDER_CANCELED("주문 취소");

        private final String message;

        OrderStatus(String message) {
            this.message = message;
        }

    }
}
