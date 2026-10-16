package com.sup.supplierbiz.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置：交换机/队列/绑定声明 + JSON 消息转换器
 *
 * @author Gabe
 * @date 2026-10-16
 */
@Configuration
@EnableRabbit
public class RabbitMQConfig {

    /**
     * 交换机
     */
    public static final String ORDER_EXCHANGE = "order.direct";
    /**
     * 队列
     */
    public static final String ORDER_NOTIFY_QUEUE = "order.notify.queue";
    /**
     * 路由键
     */
    public static final String ORDER_NOTIFY_ROUTING_KEY = "order.notified";

    /**
     * Direct 交换机（持久化）
     */
    @Bean
    public DirectExchange orderExchange() {
        return new DirectExchange(ORDER_EXCHANGE, true, false);
    }

    /**
     * 订单通知队列（durable：Broker 重启后队列定义保留；消息持久化由发送端 deliveryMode 决定）
     */
    @Bean
    public Queue orderNotifyQueue() {
        return new Queue(ORDER_NOTIFY_QUEUE, true);
    }

    /**
     * 绑定：队列 ←(routingKey)→ 交换机
     */
    @Bean
    public Binding orderNotifyBinding() {
        return BindingBuilder.bind(orderNotifyQueue())
                .to(orderExchange())
                .with(ORDER_NOTIFY_ROUTING_KEY);
    }
    /**
     * 消息转换器：声明后生产者/消费者两端自动走 JSON 序列化
     * 不声明则用 Spring AMQP 默认的 SimpleMessageConverter（Java 序列化），消息在控制台显示为不可读字节流
     */
    @Bean
    public MessageConverter messageConverter(){
        return new Jackson2JsonMessageConverter();
    }
}
