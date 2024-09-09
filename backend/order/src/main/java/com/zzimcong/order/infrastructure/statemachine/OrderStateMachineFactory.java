package com.zzimcong.order.infrastructure.statemachine;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderStatus;
import com.zzimcong.zzimconginventorycore.common.event.OrderEventType;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachineFactory {
    private final StateMachineFactory<OrderStatus, OrderEventType> stateMachineFactory;

    public OrderStateMachineFactory(StateMachineFactory<OrderStatus, OrderEventType> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public StateMachine<OrderStatus, OrderEventType> create(Order order) {
        StateMachine<OrderStatus, OrderEventType> sm = stateMachineFactory.getStateMachine(order.getId().toString());
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.resetStateMachine(new DefaultStateMachineContext<>(order.getStatus(), null, null, null));
        });
        sm.start();
        return sm;
    }
}