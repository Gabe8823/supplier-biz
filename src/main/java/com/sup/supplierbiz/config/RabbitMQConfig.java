package com.sup.supplierbiz.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置：交换机/队列/绑定声明 + JSON 消息转换器 + 生产者 Confirm/Returns 回调
 *
 * @author Gabe
 * @date 2026-10-16
 */
@Slf4j
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
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 生产者 Confirm 回调：Broker 收到消息后回调
     * <p>
     * ack=true 表示 Broker 已接收；ack=false 表示 Broker 拒收，cause 为拒收原因。
     * 必须由 @Bean 注册，Spring Boot 才会自动装配到 RabbitTemplate 上。
     */
    @Bean
    public RabbitTemplate.ConfirmCallback confirmCallback() {
        return (correlationData, ack, cause) -> {
            if (ack) {
                log.info("[MQ确认] 消息已送达Broker，correlationId={}",
                        correlationData != null ? correlationData.getId() : "无");
            } else {
                log.warn("[MQ警告] 消息未送达Broker，原因={}", cause);
                // ⚠️ Day8 再改：把失败消息存库+告警，Day7 先打印
            }
        };
    }

    /**
     * 生产者 Returns 回调：消息到达交换机、但路由不到任何队列时回调
     */
    @Bean
    public RabbitTemplate.ReturnsCallback returnsCallback() {
        return returned -> log.warn("[MQ警告] 消息路由失败，exchange={}, routingKey={}, replyText={}",
                returned.getExchange(), returned.getRoutingKey(), returned.getReplyText());
    }
}
