package com.example.rocketmq.consumer.config;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.consumer.FilterExpression;
import org.apache.rocketmq.client.apis.consumer.FilterExpressionType;
import org.apache.rocketmq.client.apis.consumer.SimpleConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

/**
 * RocketMQ V5 Consumer 配置
 */
@Configuration
public class RocketMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQConfig.class);

    @Value("${rocketmq.simple-consumer.endpoints}")
    private String endpoints;

    @Value("${rocketmq.simple-consumer.consumer-group}")
    private String consumerGroup;

    @Value("${rocketmq.simple-consumer.await-duration:30s}")
    private String awaitDuration;

    @Bean(destroyMethod = "close")
    public SimpleConsumer simpleConsumer() throws ClientException {
        logger.info("初始化 RocketMQ SimpleConsumer, endpoints: {}, consumerGroup: {}", 
                endpoints, consumerGroup);
        
        // 禁用 Epoll（Windows 系统需要）
        System.setProperty("io.grpc.netty.shaded.io.netty.transport.noNative", "true");
        
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                .enableSsl(false);  // 禁用 SSL/TLS
        
        ClientConfiguration configuration = builder.build();
        
        // 配置订阅表达式
        Map<String, FilterExpression> subscriptionExpressions = new HashMap<>();
        
        // 订阅 demo-topic，接收所有消息
        subscriptionExpressions.put("demo-topic", 
                new FilterExpression("*", FilterExpressionType.TAG));
        
        // 订阅 demo-topic-tag，只接收 tagA 或 tagB 的消息
        subscriptionExpressions.put("demo-topic-tag", 
                new FilterExpression("tagA||tagB", FilterExpressionType.TAG));
        
        // 解析 await-duration (例如 "30s" -> 30 秒)
        long awaitSeconds = parseDuration(awaitDuration);
        
        SimpleConsumer simpleConsumer = provider.newSimpleConsumerBuilder()
                .setClientConfiguration(configuration)
                .setConsumerGroup(consumerGroup)
                .setAwaitDuration(Duration.ofSeconds(awaitSeconds))
                .setSubscriptionExpressions(subscriptionExpressions)
                .build();
        
        logger.info("RocketMQ SimpleConsumer 初始化成功");
        return simpleConsumer;
    }
    
    private long parseDuration(String duration) {
        if (duration.endsWith("s")) {
            return Long.parseLong(duration.substring(0, duration.length() - 1));
        }
        return 30; // 默认 30 秒
    }
}
