package com.example.rocketmq.consumer.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * RocketMQ 消息消费者示例
 * 消费 demo-topic 主题的消息
 */
@Component
@RocketMQMessageListener(
    topic = "demo-topic",
    consumerGroup = "demo-consumer-group"
)
public class DemoMessageConsumer implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(DemoMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        logger.info("收到消息: {}", message);
        // 在这里处理业务逻辑
        try {
            // 模拟业务处理
            processMessage(message);
            logger.info("消息处理成功: {}", message);
        } catch (Exception e) {
            logger.error("消息处理失败: {}", message, e);
            // 如果抛出异常，消息会重新消费
            throw new RuntimeException("消息处理失败", e);
        }
    }

    private void processMessage(String message) {
        // 实际的业务处理逻辑
        logger.debug("处理消息内容: {}", message);
    }
}
