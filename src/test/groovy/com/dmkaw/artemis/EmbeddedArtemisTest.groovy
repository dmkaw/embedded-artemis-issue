package com.dmkaw.artemis

import groovy.util.logging.Slf4j
import org.apache.activemq.artemis.jms.client.ActiveMQQueue
import org.apache.activemq.artemis.junit.EmbeddedActiveMQResource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.jms.core.JmsTemplate
import org.springframework.test.context.ContextConfiguration
import spock.lang.Shared
import spock.lang.Specification

import javax.jms.ConnectionFactory
import javax.jms.TextMessage

@SpringBootTest(classes = EmbeddedArtemis)
@Slf4j
@ContextConfiguration(classes = [ EmbeddedArtemisConfig.class ])
class EmbeddedArtemisTest extends Specification {

    @Qualifier("testConnectionFactory")
    @Autowired
    ConnectionFactory connectionFactory

    @Shared
    EmbeddedActiveMQResource artemis = new EmbeddedActiveMQResource()

    def setup() {
        artemis.start()
    }

    def cleanup() {
        artemis.stop()
    }

    def 'should get more than one message from queue'() {
        when:

        def firstMessage = 'test message 1'
        sendMessage(firstMessage, 'test_queue')
        def firstReceivedMessage = artemis.receiveMessage('test_queue', 1000)

        println('>>>>>>>> FIRST MESSAGE:')
        println('>>>>>>>>' + firstReceivedMessage.stringBody)

        /*
        will work if artemis is restarted during test
        artemis.stop()
        artemis.start()
        */

        def secondMessage = 'test message 2'
        sendMessage(secondMessage, 'test_queue')
        def secondReceivedMessage = artemis.receiveMessage('test_queue', 1000)

        println('>>>>>>>> SECOND MESSAGE:')
        println('>>>>>>>>' + secondReceivedMessage.stringBody)


        then:
        firstMessage == firstReceivedMessage.stringBody
        secondMessage == secondReceivedMessage.stringBody
    }

    void sendMessage(String msg, String destination) {
        def template = new JmsTemplate(connectionFactory)
        template.send(new ActiveMQQueue(destination), s -> {
            TextMessage textMessage = s.createTextMessage(msg)
            String messageId = UUID.randomUUID().toString()
            textMessage.setJMSCorrelationID(messageId)
            textMessage.setJMSMessageID('ID:' + messageId)
            return textMessage
        })
    }
}
