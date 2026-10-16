package com.sup.supplierbiz.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitMQConfig {

    /** 交换机 */
    private static final String ORDER_EXCHANGE = "order.direct";
    /** 队列 */
    private static final String ORDER_NOTIFY_QUEUE = "order.notify.queue";
    /** 路由键 */
    private static final String ORDER_NOTIFY_ROUTING_KEY = "order.notified";

    /** Direct 交换机（持久化） */
    @Bean
    public DirectExchange orderExchange(){
        return new DirectExchange(ORDER_EXCHANGE,true,false);
    }
    /** 订单通知队列（持久化，Broker 重启消息不丢） */
    @Bean
    public Queue orderNotifyQueue(){
        return new Queue(ORDER_NOTIFY_QUEUE,true);
    }
    /** 绑定：队列 ←(routingKey)→ 交换机 */
    @Bean
    public Binding orderNotifyBinding(){
        return BindingBuilder.bind(orderNotifyQueue())
                .to(orderExchange())
                .with(ORDER_NOTIFY_ROUTING_KEY);
    }
}
