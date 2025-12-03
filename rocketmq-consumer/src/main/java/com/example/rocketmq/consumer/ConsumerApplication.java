package com.example.rocketmq.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RocketMQ Consumer 启动类
 */
@SpringBootApplication
public class ConsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConsumerApplication.class, args);
        System.out.println("========================================");
        System.out.println("RocketMQ Consumer 启动成功！");
        System.out.println("开始监听消息...");
        System.out.println("========================================");
    }
}
