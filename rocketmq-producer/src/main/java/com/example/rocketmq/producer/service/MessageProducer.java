package com.example.rocketmq.producer.service;

import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.message.Message;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * RocketMQ V5 消息生产者服务
 */
@Service
public class MessageProducer {

    private static final Logger logger = LoggerFactory.getLogger(MessageProducer.class);

    @Autowired
    private Producer producer;
    
    private ClientServiceProvider provider;
    
    @PostConstruct
    public void init() {
        this.provider = ClientServiceProvider.loadService();
    }

    /**
     * 发送同步消息
     */
    public void sendSyncMessage(String topic, String messageContent) {
        try {
            Message message = provider.newMessageBuilder()
                    .setTopic(topic)
                    .setBody(messageContent.getBytes(StandardCharsets.UTF_8))
                    .build();
            
            SendReceipt receipt = producer.send(message);
            logger.info("同步消息发送成功, topic: {}, messageId: {}, message: {}", 
                    topic, receipt.getMessageId(), messageContent);
        } catch (ClientException e) {
            logger.error("同步消息发送失败, topic: {}, message: {}", topic, messageContent, e);
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 发送异步消息
     */
    public void sendAsyncMessage(String topic, String messageContent) {
        try {
            Message message = provider.newMessageBuilder()
                    .setTopic(topic)
                    .setBody(messageContent.getBytes(StandardCharsets.UTF_8))
                    .build();
            
            CompletableFuture<SendReceipt> future = producer.sendAsync(message);
            future.whenComplete((receipt, throwable) -> {
                if (throwable != null) {
                    logger.error("异步消息发送失败, topic: {}, message: {}", topic, messageContent, throwable);
                } else {
                    logger.info("异步消息发送成功, topic: {}, messageId: {}, message: {}", 
                            topic, receipt.getMessageId(), messageContent);
                }
            });
        } catch (Exception e) {
            logger.error("异步消息提交失败, topic: {}, message: {}", topic, messageContent, e);
        }
    }

    /**
     * 发送单向消息（V5 不再区分单向消息，使用异步发送）
     */
    public void sendOneWayMessage(String topic, String messageContent) {
        sendAsyncMessage(topic, messageContent);
        logger.info("单向消息已提交, topic: {}, message: {}", topic, messageContent);
    }

    /**
     * 发送带标签的消息
     */
    public void sendMessageWithTag(String topic, String tag, String messageContent) {
        try {
            Message message = provider.newMessageBuilder()
                    .setTopic(topic)
                    .setTag(tag)
                    .setBody(messageContent.getBytes(StandardCharsets.UTF_8))
                    .build();
            
            SendReceipt receipt = producer.send(message);
            logger.info("带标签消息发送成功, topic: {}, tag: {}, messageId: {}, message: {}", 
                    topic, tag, receipt.getMessageId(), messageContent);
        } catch (ClientException e) {
            logger.error("带标签消息发送失败, topic: {}, tag: {}, message: {}", topic, tag, messageContent, e);
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 发送延迟消息（V5 支持自定义延迟时间，单位：秒）
     */
    public void sendDelayMessage(String topic, String messageContent, int delaySeconds) {
        try {
            long deliveryTimestamp = System.currentTimeMillis() + delaySeconds * 1000L;
            
            Message message = provider.newMessageBuilder()
                    .setTopic(topic)
                    .setBody(messageContent.getBytes(StandardCharsets.UTF_8))
                    .setDeliveryTimestamp(deliveryTimestamp)
                    .build();
            
            SendReceipt receipt = producer.send(message);
            logger.info("延迟消息发送成功, topic: {}, delaySeconds: {}, messageId: {}, message: {}", 
                    topic, delaySeconds, receipt.getMessageId(), messageContent);
        } catch (ClientException e) {
            logger.error("延迟消息发送失败, topic: {}, delaySeconds: {}, message: {}", 
                    topic, delaySeconds, messageContent, e);
            throw new RuntimeException("发送消息失败", e);
        }
    }

    /**
     * 发送顺序消息
     */
    public void sendOrderlyMessage(String topic, String messageContent, String messageGroup) {
        try {
            Message message = provider.newMessageBuilder()
                    .setTopic(topic)
                    .setMessageGroup(messageGroup)
                    .setBody(messageContent.getBytes(StandardCharsets.UTF_8))
                    .build();
            
            SendReceipt receipt = producer.send(message);
            logger.info("顺序消息发送成功, topic: {}, messageGroup: {}, messageId: {}, message: {}", 
                    topic, messageGroup, receipt.getMessageId(), messageContent);
        } catch (ClientException e) {
            logger.error("顺序消息发送失败, topic: {}, messageGroup: {}, message: {}", 
                    topic, messageGroup, messageContent, e);
            throw new RuntimeException("发送消息失败", e);
        }
    }
}
