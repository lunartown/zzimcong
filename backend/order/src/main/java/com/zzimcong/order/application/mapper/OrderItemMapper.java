package com.zzimcong.order.application.mapper;

import com.zzimcong.order.application.dto.OrderItemResponse;
import com.zzimcong.order.domain.entity.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    OrderItemResponse orderItemToOrderItemResponse(OrderItem orderItem);

    @InheritInverseConfiguration
    OrderItem orderItemResponseToOrderItem(OrderItemResponse orderItemResponse);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrderItemFromResponse(OrderItemResponse orderItemResponse, @MappingTarget OrderItem orderItem);
}