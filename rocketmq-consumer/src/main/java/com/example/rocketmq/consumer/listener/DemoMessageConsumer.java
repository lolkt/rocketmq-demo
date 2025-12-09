package com.example.rocketmq.consumer.listener;

import com.example.rocketmq.consumer.RocketMQConstants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.annotation.RocketMQMessageListener;
import org.apache.rocketmq.client.apis.consumer.ConsumeResult;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.apache.rocketmq.client.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

/**
 * RocketMQ V5 消息消费者示例
 * 使用 SimpleConsumer 拉取 demo-topic 主题的消息
 */
@Slf4j
@Component
@RequiredArgsConstructor
@RocketMQMessageListener(
        consumerGroup = RocketMQConstants.TEST_SEND_CONSUMER_GROUP,
        topic = RocketMQConstants.TEST_SEND_TOPIC,
        tag = RocketMQConstants.TEST_SEND_TAG,
        endpoints = "${rocketmq.push-consumer.endpoints}"
)
public class DemoMessageConsumer implements RocketMQListener {

    @Override
    public ConsumeResult consume(MessageView messageView) {

        String messageBody = StandardCharsets.UTF_8.decode(messageView.getBody()).toString();
        log.info("收到消息: topic={}, tag={}, messageId={}, ", RocketMQConstants.TEST_SEND_TOPIC,
                RocketMQConstants.TEST_SEND_TAG, messageBody);
        return ConsumeResult.SUCCESS;
    }
}
