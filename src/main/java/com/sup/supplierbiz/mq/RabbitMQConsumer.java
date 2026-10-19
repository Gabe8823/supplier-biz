package com.sup.supplierbiz.mq;

import com.sup.supplierbiz.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 订单通知消费者：监听 order.notify.queue，模拟异步通知供应商
 *
 * @author Gabe
 * @date 2026-10-16
 */
@Slf4j
@Component
public class RabbitMQConsumer {

    /**
     * 处理订单通知消息：解析后打印通知日志（控制台模拟，Day8 接入真实通知渠道）
     * <p>
     * 当前为自动 ack 模式：方法正常返回即确认消息。
     * Day7 先记录异常不重抛，Day8 改手动 ack + 死信队列，避免处理失败时消息静默丢失。
     *
     * @param msg 生产者发来的订单通知消息体
     */
    @RabbitListener(queues = RabbitMQConfig.ORDER_NOTIFY_QUEUE)
    public void handleNotify(Map<String, Object> msg) {
        try {
            String poNo = (String) msg.get("poNo");
            Number supplierIdNum = (Number) msg.get("supplierId");
            long supplierId = supplierIdNum == null ? 0L : supplierIdNum.longValue();
            String action = (String) msg.get("action");
            log.info("[MQ通知] 订单 {} 状态变为 {}，通知供应商（supplierId={}）", poNo, action, supplierId);
        } catch (Exception e) {
            log.error("[MQ通知] 处理失败，msg={}", msg, e);
        }
    }
}
