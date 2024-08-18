package com.zzimcong.order.infrastructure.statemachine;

import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.zzimconginventorycore.common.event.OrderEventType;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderStatus, OrderEventType> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderStatus, OrderEventType> states) throws Exception {
        states
                .withStates()
                .initial(OrderStatus.CREATED)
                .states(EnumSet.allOf(OrderStatus.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderStatus, OrderEventType> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderStatus.CREATED).target(OrderStatus.STOCK_RESERVED)
                .event(OrderEventType.INVENTORY_RESERVED)
                .and()
                .withExternal()
                .source(OrderStatus.STOCK_RESERVED).target(OrderStatus.PAYMENT_PROCESSED)
                .event(OrderEventType.PAYMENT_PROCESSED)
                .and()
                .withExternal()
                .source(OrderStatus.PAYMENT_PROCESSED).target(OrderStatus.ORDER_COMPLETED)
                .event(OrderEventType.ORDER_COMPLETED)
                .and()
                .withExternal()
                .source(OrderStatus.ORDER_COMPLETED).target(OrderStatus.PREPARING_FOR_SHIPMENT)
                .event(OrderEventType.SHIPMENT_PREPARING)
                .and()
                .withExternal()
                .source(OrderStatus.PREPARING_FOR_SHIPMENT).target(OrderStatus.SHIPPING)
                .event(OrderEventType.SHIPMENT_STARTED)
                .and()
                .withExternal()
                .source(OrderStatus.SHIPPING).target(OrderStatus.DELIVERED)
                .event(OrderEventType.SHIPMENT_DELIVERED);
    }
}