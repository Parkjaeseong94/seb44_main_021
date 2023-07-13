package re21.ieun.order.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class OrderResponseDto {

    private Long orderId;

    private Long orderNumber;

    private String status;

    private LocalDateTime createdAt;



}
