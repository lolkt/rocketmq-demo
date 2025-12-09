package com.example.rocketmq.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.client.support.DefaultListenerContainer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * RocketMQ Consumer SSL 配置
 *
 * 用于修复 rocketmq-v5-client-spring-boot 的已知 Bug:
 * @RocketMQMessageListener 注解不支持 sslEnabled 属性，
 * 导致 PushConsumer 默认使用 SSL 连接。
 *
 * 该配置通过 BeanPostProcessor 在 DefaultListenerContainer 初始化前
 * 注入 sslEnabled 配置值。
 *
 * @see <a href="https://github.com/apache/rocketmq-spring/issues/684">GitHub Issue #684</a>
 * @author cornerstone-messaging
 * @since 2025-12-08
 */
@Slf4j
@Configuration
@ConfigurationProperties(prefix = "rocketmq.push-consumer")
public class RocketMQConsumerSslConfig implements BeanPostProcessor {

    /**
     * SSL 是否启用，默认 false
     */
    private Boolean sslEnabled = false;

    public Boolean getSslEnabled() {
        return sslEnabled;
    }

    public void setSslEnabled(Boolean sslEnabled) {
        this.sslEnabled = sslEnabled;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DefaultListenerContainer) {
            DefaultListenerContainer container = (DefaultListenerContainer) bean;
            container.setSslEnabled(sslEnabled);
            log.info("[RocketMQ] 设置消费者容器 SSL 配置: beanName={}, sslEnabled={}", beanName, sslEnabled);
        }
        return bean;
    }
}
