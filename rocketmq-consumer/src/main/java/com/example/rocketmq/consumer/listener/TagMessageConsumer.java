package com.example.rocketmq.consumer.listener;

import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 带标签的消息消费者
 * 只消费带有 tagA 或 tagB 标签的消息
 */
@Component
@RocketMQMessageListener(
    topic = "demo-topic-tag",
    consumerGroup = "tag-consumer-group",
    selectorType = SelectorType.TAG,
    selectorExpression = "tagA || tagB"
)
public class TagMessageConsumer implements RocketMQListener<String> {

    private static final Logger logger = LoggerFactory.getLogger(TagMessageConsumer.class);

    @Override
    public void onMessage(String message) {
        logger.info("收到带标签的消息: {}", message);
        // 处理业务逻辑
    }
}
