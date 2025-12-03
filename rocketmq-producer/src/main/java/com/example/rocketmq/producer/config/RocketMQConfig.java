package com.example.rocketmq.producer.config;

import org.apache.rocketmq.client.apis.ClientConfiguration;
import org.apache.rocketmq.client.apis.ClientConfigurationBuilder;
import org.apache.rocketmq.client.apis.ClientException;
import org.apache.rocketmq.client.apis.ClientServiceProvider;
import org.apache.rocketmq.client.apis.producer.Producer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ V5 Producer 配置
 */
@Configuration
public class RocketMQConfig {

    private static final Logger logger = LoggerFactory.getLogger(RocketMQConfig.class);

    @Value("${rocketmq.producer.endpoints}")
    private String endpoints;

    @Value("${rocketmq.producer.topics}")
    private String topics;

    @Bean(destroyMethod = "close")
    public Producer producer() throws ClientException {
        logger.info("初始化 RocketMQ Producer, endpoints: {}, topics: {}", endpoints, topics);
        
        // 禁用 Epoll（Windows 系统需要）
        System.setProperty("io.grpc.netty.shaded.io.netty.transport.noNative", "true");
        
        ClientServiceProvider provider = ClientServiceProvider.loadService();
        
        ClientConfigurationBuilder builder = ClientConfiguration.newBuilder()
                .setEndpoints(endpoints)
                .enableSsl(false)  // 禁用 SSL/TLS
                .setRequestTimeout(java.time.Duration.ofSeconds(10));  // 增加请求超时
        
        ClientConfiguration configuration = builder.build();
        
        Producer producer = provider.newProducerBuilder()
                .setClientConfiguration(configuration)
                .setTopics(topics.split(","))
                .build();
        
        logger.info("RocketMQ Producer 初始化成功");
        return producer;
    }
}
