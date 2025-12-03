package com.example.rocketmq.producer.service;

import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

/**
 * RocketMQ 消息生产者服务
 */
@Service
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 发送同步消息
     */
    public void sendSyncMessage(String topic, String message) {
        try {
            rocketMQTemplate.syncSend(topic, message);
            logger.info("同步消息发送成功, topic: {}, message: {}", topic, message);
        } catch (Exception e) {
            logger.error("同步消息发送失败, topic: {}, message: {}", topic, message, e);
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 发送异步消息
     */
    public void sendAsyncMessage(String topic, String message) {
        rocketMQTemplate.asyncSend(topic, message, new org.apache.rocketmq.client.producer.SendCallback() {
            @Override
            public void onSuccess(org.apache.rocketmq.client.producer.SendResult sendResult) {
                logger.info("异步消息发送成功, topic: {}, message: {}, msgId: {}", 
                    topic, message, sendResult.getMsgId());
            }

            @Override
            public void onException(Throwable e) {
                logger.error("异步消息发送失败, topic: {}, message: {}", topic, message, e);
            }
        });
    }

    /**
     * 发送单向消息（不关心发送结果）
     */
    public void sendOneWayMessage(String topic, String message) {
        rocketMQTemplate.sendOneWay(topic, message);
        logger.info("单向消息发送, topic: {}, message: {}", topic, message);
    }

    /**
     * 发送带标签的消息
     */
    public void sendMessageWithTag(String topic, String tag, String message) {
        String destination = topic + ":" + tag;
        rocketMQTemplate.syncSend(destination, message);
        logger.info("带标签消息发送成功, topic: {}, tag: {}, message: {}", topic, tag, message);
    }

    /**
     * 发送延迟消息
     */
    public void sendDelayMessage(String topic, String message, int delayLevel) {
        Message<String> msg = MessageBuilder.withPayload(message).build();
        rocketMQTemplate.syncSend(topic, msg, 3000, delayLevel);
        logger.info("延迟消息发送成功, topic: {}, delayLevel: {}, message: {}", topic, delayLevel, message);
    }

    /**
     * 发送顺序消息
     */
    public void sendOrderlyMessage(String topic, String message, String hashKey) {
        rocketMQTemplate.syncSendOrderly(topic, message, hashKey);
        logger.info("顺序消息发送成功, topic: {}, hashKey: {}, message: {}", topic, hashKey, message);
    }
}
