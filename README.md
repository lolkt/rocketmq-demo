# RocketMQ 5.3.2 V5 Client Spring Boot Starter Demo

这是一个基于 Spring Boot 和 RocketMQ 5.3.2 的完整示例项目，演示了如何使用 RocketMQ V5 Client Spring Boot Starter 进行消息的生产和消费。

> **重要更新**: 本项目已从 `rocketmq-spring-boot-starter` 迁移到 `rocketmq-v5-client-spring-boot-starter`。查看 [迁移指南](V5_CLIENT_MIGRATION_GUIDE.md) 了解详情。

## 项目信息

- **RocketMQ 版本**: 5.3.2
- **Spring Boot 版本**: 2.7.18
- **JDK 版本**: 1.8
- **RocketMQ V5 Client Spring Boot Starter**: 5.3.2

## RocketMQ 集群信息

- **内网访问地址**: rocketmq-name-server-service.comm-plugins:9876
- **集群外访问地址**: 192.168.1.53:31637
- **Dashboard**: http://192.168.1.53:30817/#/message

> 本项目配置使用集群外访问地址：192.168.1.53:31637

## 项目结构

```
rocketmq-demo/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/
│       │       └── example/
│       │           └── rocketmq/
│       │               ├── RocketMqDemoApplication.java    # 主启动类
│       │               ├── controller/
│       │               │   └── MessageController.java      # REST API 控制器
│       │               ├── producer/
│       │               │   └── MessageProducer.java        # 消息生产者
│       │               ├── consumer/
│       │               │   ├── DemoMessageConsumer.java    # 普通消息消费者
│       │               │   └── TagMessageConsumer.java     # 带标签消息消费者
│       │               └── model/
│       │                   └── MessageRequest.java         # 消息请求对象
│       └── resources/
│           └── application.yml                             # 配置文件
├── pom.xml                                                 # Maven 配置
└── README.md                                               # 项目说明文档
```

## 功能特性

### 消息生产者功能

- ✅ 同步消息发送
- ✅ 异步消息发送
- ✅ 单向消息发送
- ✅ 带标签的消息发送
- ✅ 延迟消息发送
- ✅ 顺序消息发送

### 消息消费者功能

- ✅ 普通消息消费
- ✅ 带标签的消息过滤消费

## 快速开始

### 1. 环境要求

- JDK 1.8 或更高版本
- Maven 3.x
- RocketMQ 5.3.2 服务器（已部署）

### 2. 配置说明

编辑 `src/main/resources/application.yml` 文件，确认 RocketMQ V5 Client 配置：

```yaml
rocketmq:
  endpoints: 192.168.1.53:31637     # RocketMQ NameServer 地址
  producer:
    topics: demo-topic,demo-topic-tag  # 需要发送消息的 topic 列表
    request-timeout: 10s               # 请求超时时间
  simple-consumer:
    enabled: true
    consumer-group: demo-consumer-group
    subscription-expressions:
      demo-topic: '*'                  # 订阅 demo-topic 的所有消息
      demo-topic-tag: '*'              # 订阅 demo-topic-tag 的所有消息
```

### 3. 编译项目

```bash
mvn clean package
```

### 4. 运行项目

```bash
mvn spring-boot:run
```

或者运行打包后的 jar 文件：

```bash
java -jar target/rocketmq-demo-1.0.0-SNAPSHOT.jar
```

项目启动后，默认端口为 8080。

## API 接口说明

### 1. 健康检查

```bash
GET http://localhost:8080/api/message/health
```

### 2. 发送同步消息

```bash
POST http://localhost:8080/api/message/send/sync
Content-Type: application/x-www-form-urlencoded

topic=demo-topic&message=Hello RocketMQ
```

### 3. 发送异步消息

```bash
POST http://localhost:8080/api/message/send/async
Content-Type: application/x-www-form-urlencoded

topic=demo-topic&message=Async Message
```

### 4. 发送单向消息

```bash
POST http://localhost:8080/api/message/send/oneway
Content-Type: application/x-www-form-urlencoded

topic=demo-topic&message=OneWay Message
```

### 5. 发送带标签的消息

```bash
POST http://localhost:8080/api/message/send/tag
Content-Type: application/x-www-form-urlencoded

topic=demo-topic-tag&tag=tagA&message=Tagged Message
```

### 6. 发送延迟消息

```bash
POST http://localhost:8080/api/message/send/delay
Content-Type: application/x-www-form-urlencoded

topic=demo-topic&message=Delay Message&delaySeconds=10
```

> **注意**: V5 Client 使用 `delaySeconds` 参数（秒数）代替旧版本的 `delayLevel`（延迟级别）。

延迟时间示例：
- 10 秒：`delaySeconds=10`
- 1 分钟：`delaySeconds=60`
- 5 分钟：`delaySeconds=300`
- 1 小时：`delaySeconds=3600`
- 2 小时：`delaySeconds=7200`

### 7. 发送顺序消息

```bash
POST http://localhost:8080/api/message/send/orderly
Content-Type: application/x-www-form-urlencoded

topic=demo-topic&message=Orderly Message&hashKey=order123
```

## 使用 cURL 测试

### 发送同步消息

```bash
curl -X POST "http://localhost:8080/api/message/send/sync" \
  -d "topic=demo-topic" \
  -d "message=Hello RocketMQ from cURL"
```

### 发送带标签的消息

```bash
curl -X POST "http://localhost:8080/api/message/send/tag" \
  -d "topic=demo-topic-tag" \
  -d "tag=tagA" \
  -d "message=Tagged message from cURL"
```

## 使用 Postman 测试

1. 导入以下请求到 Postman
2. 选择 POST 方法
3. URL: `http://localhost:8080/api/message/send/sync`
4. Body 选择 `x-www-form-urlencoded`
5. 添加参数：
   - `topic`: demo-topic
   - `message`: Hello RocketMQ

## 查看消息

访问 RocketMQ Dashboard 查看消息：

http://192.168.1.53:30817/#/message

## 日志说明

应用启动后，可以在控制台看到以下日志：

- **生产者日志**: 显示消息发送成功/失败信息
- **消费者日志**: 显示消息接收和处理信息

示例日志：

```
INFO  c.e.r.producer.MessageProducer - 同步消息发送成功, topic: demo-topic, message: Hello RocketMQ
INFO  c.e.r.consumer.DemoMessageConsumer - 收到消息: Hello RocketMQ
INFO  c.e.r.consumer.DemoMessageConsumer - 消息处理成功: Hello RocketMQ
```

## 常见问题

### 1. 连接 RocketMQ 失败

- 检查 NameServer 地址是否正确
- 确认网络连接是否正常
- 检查防火墙设置

### 2. 消息发送失败

- 检查 Topic 是否已创建
- 确认生产者组配置是否正确
- 查看 RocketMQ Dashboard 中的 Topic 配置

### 3. 消费者无法接收消息

- 检查消费者组配置
- 确认 Topic 和 Tag 配置是否匹配
- 查看消费者是否正常启动

## 进阶配置

### 自定义生产者配置

在 `application.yml` 中可以配置更多生产者参数：

```yaml
rocketmq:
  producer:
    topics: demo-topic,demo-topic-tag
    request-timeout: 10s                  # 请求超时时间
    max-message-size: 4194304             # 最大消息大小（4MB）
    transaction-checker:
      enabled: false                      # 是否启用事务消息检查
```

### 自定义消费者配置

在 `application.yml` 中配置 SimpleConsumer：

```yaml
rocketmq:
  simple-consumer:
    enabled: true
    consumer-group: demo-consumer-group
    await-duration: 30s                   # 长轮询等待时间
    subscription-expressions:
      demo-topic: '*'                     # 订阅所有消息
      demo-topic-tag: 'tagA||tagB'        # 订阅特定标签
```

> **注意**: V5 Client 使用 Pull 模式的 SimpleConsumer，需要在代码中主动拉取消息。

## 参考资料

- [RocketMQ 官方文档](https://rocketmq.apache.org/)
- [RocketMQ V5 Client GitHub](https://github.com/apache/rocketmq-clients)
- [RocketMQ Spring Boot Starter](https://github.com/apache/rocketmq-spring)
- [Spring Boot 官方文档](https://spring.io/projects/spring-boot)
- [V5 Client 迁移指南](V5_CLIENT_MIGRATION_GUIDE.md) - 从旧版本迁移到 V5 Client 的完整指南

## 许可证

MIT License

## 联系方式

如有问题，请访问 RocketMQ Dashboard: http://192.168.1.53:30817/#/message
