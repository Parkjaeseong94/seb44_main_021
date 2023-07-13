package re21.ieun.order.mapper;


import org.mapstruct.Mapper;
import re21.ieun.order.dto.OrderPatchDto;
import re21.ieun.order.dto.OrderPostDto;
import re21.ieun.order.entity.Order;

@Mapper(componentModel = "spring")
public interface OrderMapper {
    Order order(OrderPostDt )
}
