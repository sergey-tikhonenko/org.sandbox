/**
 *
 */
package org.sandbox.messaging.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JMS Consumer
 *
 * @author tserge, <a href="mailto:Sergey.Tikhonenko@exigenservices.com">Sergey Tikhonenko</a>
 * @since 22 Aug 2012
 *
 */
public class Consumer implements MessageListener {

    public static String brokerURL = "tcp://localhost:61616";

    /** Logger declaration */
    private final Logger logger = LoggerFactory.getLogger(Consumer.class);

    /**
     * main
     * @param args
     */
    public static void main(final String[] args) {
        new Consumer().start();
    }

    /**
     * start Consumer to listen messages
     */
    public void start() {
        try {
            ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);
            Connection connection = factory.createConnection();
            connection.start();

            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Destination destination = session.createQueue("test");

            MessageConsumer consumer = session.createConsumer(destination);
            consumer.setMessageListener(this);
        }
        catch (Exception e) {
            logger.error("Start failed!", e);
        }
    }

    /* (non-Javadoc)
     * @see javax.jms.MessageListener#onMessage(javax.jms.Message)
     */
    public void onMessage(final Message message) {
        try {
            if (message instanceof TextMessage) {
                TextMessage txtMessage = (TextMessage)message;
                System.out.println("Message received: " + txtMessage.getText());
            }
            else {
                logger.warn("Invalid message received.");
            }
        }
        catch (JMSException e) {
            logger.error("Message receiving failed!", e);
        }
    }
}
