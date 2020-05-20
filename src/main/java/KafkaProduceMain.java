import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class KafkaProduceMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入KAFKA服务器信息，格式：IP:PORT，如10.1.2.3:9092");
        Properties properties = new Properties();
        properties.put("bootstrap.servers", scanner.nextLine());
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        KafkaProducer<String, String> kafkaProducer = new KafkaProducer<String, String>(properties);
        System.out.println("输入TOPIC名称");
        String topic = scanner.nextLine();
        while (true) {
            System.out.println("输入消息");
            Future<RecordMetadata> recordMetadataFuture = kafkaProducer.send(new ProducerRecord<String, String>(topic, scanner.nextLine()));
            RecordMetadata recordMetadata = recordMetadataFuture.get();
//            System.out.println("recordMetadataFuture.get() = " + recordMetadata);
        }
    }
}
