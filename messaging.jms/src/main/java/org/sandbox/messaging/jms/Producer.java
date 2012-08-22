/**
 *
 */
package org.sandbox.messaging.jms;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Producer
 *
 * @author tserge, <a href="mailto:Sergey.Tikhonenko@exigenservices.com">Sergey Tikhonenko</a>
 * @since 22 Aug 2012
 *
 */
public class Producer {

    public static String brokerURL = "tcp://localhost:61616";

    /** Logger declaration */
    private final Logger logger = LoggerFactory.getLogger(Producer.class);

    private final Connection connection;
    private final Session session;
    private final MessageProducer producer;

    public static void main( final String[] args ) throws Exception {
        // setup the connection to ActiveMQ
        ConnectionFactory factory = new ActiveMQConnectionFactory(brokerURL);

        Producer producer = new Producer(factory, "test");
        producer.start();
        producer.close();
    }

    public Producer(final ConnectionFactory factory, final String queueName) throws JMSException {

        connection = factory.createConnection();
        connection.start();

        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(queueName);

        producer = session.createProducer(destination);
    }

    public void start() throws JMSException {

        for (int i = 0; i < 100; i++) {
            logger.info("Creating Message {}", i);
            Message message = session.createTextMessage("Hello World!");
            producer.send(message);
        }
    }

    public void close() throws JMSException {
        if (connection != null) {
            connection.close();
        }
    }
}
