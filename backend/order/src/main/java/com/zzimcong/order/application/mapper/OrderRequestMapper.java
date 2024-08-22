package com.zzimcong.order.application.mapper;

import com.zzimcong.order.application.dto.OrderItemRequest;
import com.zzimcong.order.application.dto.OrderRequest;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", constant = "CREATED")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "orderItems", source = "items")
    Order orderRequestToOrder(OrderRequest orderRequest);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    OrderItem orderItemRequestToOrderItem(OrderItemRequest orderItemRequest);

    @AfterMapping
    default void linkOrderItems(@MappingTarget Order order) {
        if (order.getOrderItems() != null) {
            order.getOrderItems().forEach(item -> item.setOrder(order));
        }
    }

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateOrderFromRequest(OrderRequest orderRequest, @MappingTarget Order order);
}