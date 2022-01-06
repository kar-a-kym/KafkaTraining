package com.epam.kafka.producer;

import com.epam.twitter.TwitterStreamReader;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class MessageProducer {

    private Properties props;
    private static final String CONFIG_PATH = "producer.properties";
    private static final String TOPIC = "tweets";

    @Autowired
    private TwitterStreamReader twitterStreamReader;


    public static void main(String[] args) throws IOException {
        Properties props = new Properties();
        try (InputStream inputStream = ClassLoader.getSystemResourceAsStream(CONFIG_PATH)) {
            props.load(inputStream);
        }
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(props)) {
            ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC, "12345t66");
            List<Integer> partitions = new ArrayList<>();
            for (int i = 0; i < 10000; i++) {
                Future<RecordMetadata> send = producer.send(record);
                partitions.add(send.get().partition());
            }
            System.out.println("0 part: " + Collections.frequency(partitions, 0));
            System.out.println("1 part: " + Collections.frequency(partitions, 1));
            System.out.println("2 part: " + Collections.frequency(partitions, 2));
            System.out.println("3 part: " + Collections.frequency(partitions, 3));
            producer.flush();
        } catch (ExecutionException | InterruptedException e) {
            System.out.println(e);
        }
    }

}
