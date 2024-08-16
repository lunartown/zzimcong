package com.zzimcong.order.infrastructure.statemachine;

import com.zzimcong.order.domain.entity.Order;
import com.zzimcong.order.domain.entity.OrderEventType;
import com.zzimcong.order.domain.entity.OrderState;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Component;

@Component
public class OrderStateMachineFactory {
    private final StateMachineFactory<OrderState, OrderEventType> stateMachineFactory;

    public OrderStateMachineFactory(StateMachineFactory<OrderState, OrderEventType> stateMachineFactory) {
        this.stateMachineFactory = stateMachineFactory;
    }

    public StateMachine<OrderState, OrderEventType> create(Order order) {
        StateMachine<OrderState, OrderEventType> sm = stateMachineFactory.getStateMachine(order.getId().toString());
        sm.stop();
        sm.getStateMachineAccessor().doWithAllRegions(sma -> {
            sma.resetStateMachine(new DefaultStateMachineContext<>(order.getState(), null, null, null));
        });
        sm.start();
        return sm;
    }
}