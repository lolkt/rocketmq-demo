package com.example.rocketmq.producer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * RocketMQ Producer 启动类
 */
@SpringBootApplication
public class ProducerApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProducerApplication.class, args);
        System.out.println("========================================");
        System.out.println("RocketMQ Producer 启动成功！");
        System.out.println("访问地址: http://localhost:8080");
        System.out.println("健康检查: http://localhost:8080/api/message/health");
        System.out.println("========================================");
    }
}
