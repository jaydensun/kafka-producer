package com.jayden.kafka.swing;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Future;

public class KafkaSender {
    private static Logger logger = LoggerFactory.getLogger(KafkaSender.class);

    public static SendResult send(String broker, String topic, String msg, boolean oneMsg, boolean trim) {
        KafkaProducer<String, String> kafkaProducer = null;
        try {
            Properties properties = new Properties();
            broker = broker.trim();
            properties.put("bootstrap.servers", broker);
            properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
            kafkaProducer = new KafkaProducer<String, String>(properties);
            topic = topic.trim();
            List<String> msgs = processMsg(msg, oneMsg, trim);
            logger.info("broker: {}, topic: {}, msg count: {}, msgs: {}", broker, topic, msgs.size(), msgs);
            if (msgs.isEmpty()) {
                return errorSendResult("消息为空");
            }
            for (String message : msgs) {
                Future<RecordMetadata> recordMetadataFuture = kafkaProducer.send(new ProducerRecord<>(topic, message));
                recordMetadataFuture.get();
            }
            SendResult sendResult = new SendResult();
            sendResult.setSuccess(true);
            sendResult.setCount(msgs.size());
            return sendResult;
        } catch (Exception e) {
            return errorSendResult(e.getMessage());
        } finally {
            if (kafkaProducer != null) {
                kafkaProducer.close();
            }
        }
    }

    private static SendResult errorSendResult(String message) {
        SendResult sendResult = new SendResult();
        sendResult.setSuccess(false);
        sendResult.setErrorMsg(message);
        return sendResult;
    }

    private static List<String> processMsg(String msg, boolean oneMsg, boolean trim) {
        ArrayList<String> list = new ArrayList<String>();
        if (oneMsg) {
            if (trim) msg = trim(msg);
            if (!msg.isEmpty()) {
                list.add(msg);
            }
        } else {
            String[] strings = msg.split(isWindows() ? "\r\n" : "\n");
            for (String string : strings) {
                if (trim) string = trim(string);
                if (!string.isEmpty()) {
                    list.add(string);
                }
            }
        }
        return list;
    }

    private static String trim(String string) {
        return string.replaceFirst("^\\s*", "").replaceFirst("\\s*$", "");
    }

    public static boolean isWindows() {
        return System.getProperty("os.name").toLowerCase().contains("windows");
    }

}
