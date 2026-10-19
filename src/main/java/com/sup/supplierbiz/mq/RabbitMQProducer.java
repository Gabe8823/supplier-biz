package com.sup.supplierbiz.mq;

import com.sup.supplierbiz.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;

/**
 * 订单通知生产者：订单状态流转后，向 RabbitMQ 发送供应商通知消息
 *
 * @author Gabe
 * @date 2026-10-16
 */
@RequiredArgsConstructor
@Component
public class RabbitMQProducer {
    private final RabbitTemplate rabbitTemplate;

    public void notifySupplier(String poNo, Long supplierId, String action, Long operatorId) {
        HashMap<String, Object> msg = new HashMap<>();
        msg.put("poNo", poNo);
        msg.put("supplierId", supplierId);
        msg.put("action", action);
        msg.put("operatorId", operatorId);
        msg.put("timestamp", LocalDateTime.now().toString());

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.ORDER_EXCHANGE,
                RabbitMQConfig.ORDER_NOTIFY_ROUTING_KEY,
                msg
        );
    }
}
