package com.example.rocketmq.producer.controller;

import com.example.rocketmq.producer.service.MessageProducer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息发送控制器
 */
@RestController
@RequestMapping("/api/message")
public class MessageController {

    @Autowired
    private MessageProducer messageProducer;

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
    public Map<String, Object> sendSyncMessage(
            @RequestParam(defaultValue = "demo-topic") String topic,
            @RequestParam String message) {
        Map<String, Object> result = new HashMap<>();
        try {
            messageProducer.sendSyncMessage(topic, message);
            result.put("success", true);
            result.put("message", "同步消息发送成功");
            result.put("topic", topic);
            result.put("content", message);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送异步消息
     */
    @PostMapping("/send/async")
    public Map<String, Object> sendAsyncMessage(
            @RequestParam(defaultValue = "demo-topic") String topic,
            @RequestParam String message) {
        Map<String, Object> result = new HashMap<>();
        try {
            messageProducer.sendAsyncMessage(topic, message);
            result.put("success", true);
            result.put("message", "异步消息已提交");
            result.put("topic", topic);
            result.put("content", message);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送单向消息
     */
    @PostMapping("/send/oneway")
    public Map<String, Object> sendOneWayMessage(
            @RequestParam(defaultValue = "demo-topic") String topic,
            @RequestParam String message) {
        Map<String, Object> result = new HashMap<>();
        try {
            messageProducer.sendOneWayMessage(topic, message);
            result.put("success", true);
            result.put("message", "单向消息已发送");
            result.put("topic", topic);
            result.put("content", message);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送带标签的消息
     */
    @PostMapping("/send/tag")
    public Map<String, Object> sendMessageWithTag(
            @RequestParam(defaultValue = "demo-topic-tag") String topic,
            @RequestParam String tag,
            @RequestParam String message) {
        Map<String, Object> result = new HashMap<>();
        try {
            messageProducer.sendMessageWithTag(topic, tag, message);
            result.put("success", true);
            result.put("message", "带标签消息发送成功");
            result.put("topic", topic);
            result.put("tag", tag);
            result.put("content", message);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送延迟消息
     */
    @PostMapping("/send/delay")
    public Map<String, Object> sendDelayMessage(
            @RequestParam(defaultValue = "demo-topic") String topic,
            @RequestParam String message,
            @RequestParam(defaultValue = "3") int delayLevel) {
        Map<String, Object> result = new HashMap<>();
        try {
            messageProducer.sendDelayMessage(topic, message, delayLevel);
            result.put("success", true);
            result.put("message", "延迟消息发送成功");
            result.put("topic", topic);
            result.put("delayLevel", delayLevel);
            result.put("content", message);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败: " + e.getMessage());
        }
        return result;
    }

    /**
     * 发送顺序消息
     */
    @PostMapping("/send/orderly")
    public Map<String, Object> sendOrderlyMessage(
            @RequestParam(defaultValue = "demo-topic") String topic,
            @RequestParam String message,
            @RequestParam String hashKey) {
        Map<String, Object> result = new HashMap<>();
        try {
            messageProducer.sendOrderlyMessage(topic, message, hashKey);
            result.put("success", true);
            result.put("message", "顺序消息发送成功");
            result.put("topic", topic);
            result.put("hashKey", hashKey);
            result.put("content", message);
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "发送失败: " + e.getMessage());
        }
        return result;
    }
}
