package com.zzimcong.order.application.mapper;

import com.zzimcong.order.application.dto.OrderResponse;
import com.zzimcong.order.domain.entity.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {OrderItemMapper.class})
public interface OrderMapper {

    @Mapping(target = "items", source = "orderItems")
    OrderResponse orderToOrderResponse(Order order);
}