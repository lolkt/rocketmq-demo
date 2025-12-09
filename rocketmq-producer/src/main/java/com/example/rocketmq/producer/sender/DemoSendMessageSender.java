package com.example.rocketmq.producer.sender;


import com.example.rocketmq.producer.RocketMQConstants;
import com.example.rocketmq.producer.model.DemoSendMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.apis.producer.SendReceipt;
import org.apache.rocketmq.client.core.RocketMQClientTemplate;
import org.springframework.stereotype.Component;

/**
 * 发送消息发送器
 * 使用 rocketmq-v5-client-spring-boot 的 RocketMQClientTemplate 发送消息
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DemoSendMessageSender {

    private final RocketMQClientTemplate rocketMQClientTemplate;

    /**
     * 发送发送消息到MQ
     *
     * @param message 发送消息
     */
    public void send(DemoSendMessage message) {
        try {
            // 使用 topic:tag 格式
            String destination = RocketMQConstants.TEST_SEND_TOPIC + ":" + RocketMQConstants.TEST_SEND_TAG;
            SendReceipt receipt = rocketMQClientTemplate.syncSendNormalMessage(destination, message);

            log.info("[DemoSendMessageSender] 消息发送成功, recordId={}, bizId={}, messageId={}",
                    message.getRecordId(), message.getBizId(), receipt.getMessageId());
        } catch (Exception e) {

            throw new RuntimeException("Failed to send email message to MQ");
        }
    }
}
