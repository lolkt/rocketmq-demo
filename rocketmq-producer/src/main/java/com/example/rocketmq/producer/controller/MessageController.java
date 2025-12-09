package com.example.rocketmq.producer.controller;

import com.example.rocketmq.producer.model.DemoSendMessage;
import com.example.rocketmq.producer.sender.DemoSendMessageSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息发送控制器
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {
    @Autowired
    public DemoSendMessageSender emailSendMessageSender;

    /**
     * 健康检查
     */
    @GetMapping("/health")
    public Map<String, Object> health() {
        Map<String, Object> result = new HashMap<>();
        result.put("status", "UP");
        result.put("service", "RocketMQ Producer");
        result.put("version", "1.0.0");
        return result;
    }

    /**
     * 发送同步消息
     */
    @PostMapping("/send/sync")
    public Map<String, Object> sendSyncMessage() {
        Map<String, Object> result = new HashMap<>();
        try {

            DemoSendMessage emailSendMessage = DemoSendMessage.builder()
                    .recordId(1L)
                    .bizId("2")
                    .build();

            emailSendMessageSender.send(emailSendMessage);
            result.put("success", true);
            result.put("message", "同步消息发送成功");

        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败: " + e.getMessage());
        }
        return result;
    }

}
