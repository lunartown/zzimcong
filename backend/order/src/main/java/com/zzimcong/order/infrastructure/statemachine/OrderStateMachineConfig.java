package com.zzimcong.order.infrastructure.statemachine;

import com.zzimcong.order.domain.entity.OrderEventType;
import com.zzimcong.order.domain.entity.OrderState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

import java.util.EnumSet;

@Configuration
@EnableStateMachineFactory
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, OrderEventType> {

    @Override
    public void configure(StateMachineStateConfigurer<OrderState, OrderEventType> states) throws Exception {
        states
                .withStates()
                .initial(OrderState.CREATED)
                .states(EnumSet.allOf(OrderState.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<OrderState, OrderEventType> transitions) throws Exception {
        transitions
                .withExternal()
                .source(OrderState.CREATED).target(OrderState.STOCK_RESERVED)
                .event(OrderEventType.RESERVE_STOCK)
                .and()
                .withExternal()
                .source(OrderState.STOCK_RESERVED).target(OrderState.PAYMENT_PROCESSED)
                .event(OrderEventType.PROCESS_PAYMENT)
                .and()
                .withExternal()
                .source(OrderState.PAYMENT_PROCESSED).target(OrderState.ORDER_COMPLETED)
                .event(OrderEventType.COMPLETE_ORDER)
                .and()
                .withExternal()
                .source(OrderState.ORDER_COMPLETED).target(OrderState.PREPARING_FOR_SHIPMENT)
                .event(OrderEventType.PREPARE_SHIPMENT)
                .and()
                .withExternal()
                .source(OrderState.PREPARING_FOR_SHIPMENT).target(OrderState.SHIPPING)
                .event(OrderEventType.SHIP_ORDER)
                .and()
                .withExternal()
                .source(OrderState.SHIPPING).target(OrderState.DELIVERED)
                .event(OrderEventType.DELIVER_ORDER)
                .and()
                .withExternal()
                .source(OrderState.DELIVERED).target(OrderState.ORDER_CONFIRMED)
                .event(OrderEventType.CONFIRM_ORDER)
                .and()
                .withExternal()
                .source(OrderState.DELIVERED).target(OrderState.REFUND_REQUESTED)
                .event(OrderEventType.REQUEST_REFUND)
                .and()
                .withExternal()
                .source(OrderState.REFUND_REQUESTED).target(OrderState.REFUND_COMPLETED)
                .event(OrderEventType.COMPLETE_REFUND)
                .and()
                .withExternal()
                .source(OrderState.CREATED).target(OrderState.CANCELED)
                .event(OrderEventType.CANCEL_ORDER)
                .and()
                .withExternal()
                .source(OrderState.STOCK_RESERVED).target(OrderState.CANCELED)
                .event(OrderEventType.CANCEL_ORDER)
                .and()
                .withExternal()
                .source(OrderState.PAYMENT_PROCESSED).target(OrderState.CANCELED)
                .event(OrderEventType.CANCEL_ORDER);
    }
}