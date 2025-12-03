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
import java.util.Optional;

/**
 * RocketMQ V5 带标签的消息消费者
 * 消费 demo-topic-tag 主题中带有 tagA 或 tagB 标签的消息
 * 注意：Tag 过滤在配置文件中通过 subscription-expressions 设置
 */
@Component
public class TagMessageConsumer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(TagMessageConsumer.class);

    @Autowired
    private SimpleConsumer simpleConsumer;

    @Override
    public void run(String... args) {
        // 启动消费线程
        Thread consumerThread = new Thread(() -> {
            logger.info("TagMessageConsumer 开始消费带标签的消息...");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // 拉取消息
                    List<MessageView> messages = simpleConsumer.receive(10, Duration.ofSeconds(30));
                    
                    for (MessageView message : messages) {
                        try {
                            String body = StandardCharsets.UTF_8.decode(message.getBody()).toString();
                            Optional<String> tag = message.getTag();
                            
                            logger.info("收到带标签的消息: topic={}, tag={}, messageId={}, body={}", 
                                    message.getTopic(), tag.orElse("无标签"), message.getMessageId(), body);
                            
                            // 处理业务逻辑
                            processMessage(body, tag.orElse(""));
                            
                            // 确认消息
                            simpleConsumer.ack(message);
                            logger.info("带标签消息处理成功: tag={}, body={}", tag.orElse("无标签"), body);
                            
                        } catch (Exception e) {
                            logger.error("带标签消息处理失败: messageId={}", message.getMessageId(), e);
                        }
                    }
                } catch (Exception e) {
                    logger.error("拉取带标签消息失败", e);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        break;
                    }
                }
            }
        });
        
        consumerThread.setName("TagMessageConsumer-Thread");
        consumerThread.setDaemon(true);
        consumerThread.start();
    }

    private void processMessage(String message, String tag) {
        // 实际的业务处理逻辑
        logger.debug("处理带标签的消息: tag={}, message={}", tag, message);
    }
}
