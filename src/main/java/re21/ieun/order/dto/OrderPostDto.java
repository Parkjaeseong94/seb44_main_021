package re21.ieun.order.dto;

import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter
public class OrderPostDto {

    @NotEmpty
    private List<OrderPostDto> orderProducts;

    @Positive
    private Long OrderId;

}
