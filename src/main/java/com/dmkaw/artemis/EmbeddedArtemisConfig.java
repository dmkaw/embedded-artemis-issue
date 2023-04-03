package com.dmkaw.artemis;

import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.ConnectionFactory;

@Configuration
public class EmbeddedArtemisConfig {

    @Bean
    ConnectionFactory testConnectionFactory(@Value("vm://0") String providerUrl,
                                            @Value("2") int threadPool) {
        ActiveMQConnectionFactory amqFactory = new ActiveMQConnectionFactory(providerUrl);
        amqFactory.setScheduledThreadPoolMaxSize(threadPool);
        return amqFactory;
    }
}
