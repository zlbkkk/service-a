package com.example.servicea.config;

// /**
//  * 【类完全注释】RabbitMQ 工厂配置类
//  * 
//  * 模拟 DbassRabbitFactory 的变更类型
//  * 整个类被注释掉，用于测试分析系统能否识别类级别的注释
//  * 
//  * @author system
//  * @version 1.0
//  */
// @Configuration
// public class DbassRabbitFactory {
// 
//     @Value("${rabbitmq.host:localhost}")
//     private String host;
// 
//     @Value("${rabbitmq.port:5672}")
//     private int port;
// 
//     @Value("${rabbitmq.username:guest}")
//     private String username;
// 
//     @Value("${rabbitmq.password:guest}")
//     private String password;
// 
//     /**
//      * 创建 RabbitMQ 连接工厂
//      */
//     @Bean
//     public ConnectionFactory connectionFactory() {
//         CachingConnectionFactory factory = new CachingConnectionFactory();
//         factory.setHost(host);
//         factory.setPort(port);
//         factory.setUsername(username);
//         factory.setPassword(password);
//         return factory;
//     }
// 
//     /**
//      * 创建 RabbitTemplate
//      */
//     @Bean
//     public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
//         RabbitTemplate template = new RabbitTemplate(connectionFactory);
//         template.setMessageConverter(new Jackson2JsonMessageConverter());
//         return template;
//     }
// }

// 注意：整个类已被注释，RabbitMQ 配置将失效
// 这是一个严重的变更，可能导致消息队列功能不可用
