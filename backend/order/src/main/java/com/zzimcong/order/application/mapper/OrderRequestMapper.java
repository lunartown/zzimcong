package com.zzimcong.order.application.mapper;

import com.zzimcong.order.application.dto.*;
import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderAddress;
import com.zzimcong.order.domain.entity.OrderItem;
import com.zzimcong.order.domain.entity.PaymentDetails;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface OrderRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userId", ignore = true)
    @Mapping(target = "status", constant = "TEMP")
    @Mapping(target = "deleted", constant = "false")
    @Mapping(target = "orderItems", source = "items")
    Order orderPreperationRequestToOrder(OrderPreparationRequest orderPreparationRequest);

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
    @Mapping(source = "orderAddressRequest", target = "orderAddress")
    @Mapping(source = "paymentDetailsRequest", target = "paymentDetails")
    void updateOrderFromRequest(OrderCreationRequest orderCreationRequest, @MappingTarget Order order);

    @Mapping(target = "id", ignore = true)
    OrderAddress orderAddressRequestToOrderAddress(OrderAddressRequest orderAddressRequest);

    @Mapping(target = "id", ignore = true)
    PaymentDetails paymentDetailsRequestToPaymentDetails(PaymentDetailsRequest paymentDetailsRequest);

    @AfterMapping
    default void logMappingResult(OrderCreationRequest request, @MappingTarget Order order) {
        System.out.println("OrderCreationRequest: " + request);
        System.out.println("Mapped Order: " + order);
        System.out.println("PaymentDetails: " + order.getPaymentDetails());
    }
}