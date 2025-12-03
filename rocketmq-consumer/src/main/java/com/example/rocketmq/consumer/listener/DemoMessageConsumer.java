package com.example.rocketmq.consumer.listener;

import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.apache.rocketmq.client.apis.message.MessageView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * RocketMQ V5 消息消费者示例
 * 使用 SimpleConsumer 拉取 demo-topic 主题的消息
 */
@Component
public class DemoMessageConsumer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DemoMessageConsumer.class);

    @Autowired
    private SimpleConsumer simpleConsumer;

    @Override
    public void run(String... args) {
        // 启动消费线程
        Thread consumerThread = new Thread(() -> {
            logger.info("DemoMessageConsumer 开始消费消息...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 拉取消息，最多等待 30 秒
                    List<MessageView> messages = simpleConsumer.receive(10, Duration.ofSeconds(30));
                    
                    for (MessageView message : messages) {
                        try {
                            String body = StandardCharsets.UTF_8.decode(message.getBody()).toString();
                            logger.info("收到消息: topic={}, messageId={}, body={}", 
                                    message.getTopic(), message.getMessageId(), body);
                            
                            // 处理业务逻辑
                            processMessage(body);
                            
                            // 确认消息（ACK）
                            simpleConsumer.ack(message);
                            logger.info("消息处理成功并已确认: {}", body);
                            
                        } catch (Exception e) {
                            logger.error("消息处理失败: messageId={}", message.getMessageId(), e);
                            // 不 ACK，消息会重新投递
                        }
                    }
                } catch (Exception e) {
                    logger.error("拉取消息失败", e);
                    try {
                        Thread.sleep(1000); // 失败后等待 1 秒
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        
        consumerThread.setName("DemoMessageConsumer-Thread");
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    private void processMessage(String message) {
        // 实际的业务处理逻辑
        logger.debug("处理消息内容: {}", message);
    }
}
