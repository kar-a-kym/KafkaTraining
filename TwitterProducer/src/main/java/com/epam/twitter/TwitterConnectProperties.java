package com.epam.twitter;


import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Getter
@Configuration
@PropertySource("classpath:tweeter.properties")
public class TwitterConnectProperties {

    @Value("{consumer.key}")
    private String consumerKey;
    @Value("{consumer.secret}")
    private String consumerSecret;
    @Value("{token}")
    private String token;
    @Value("{token.secret}")
    private String tokenSecret;
}
//bin\windows\kafka-topics.bat --create --partitions 4 --replication-factor 3 --topic tweets --bootstrap-server localhost:9092