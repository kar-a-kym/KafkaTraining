package com.epam.twitter;

import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwitterConfiguration {

    @Autowired
    TwitterConnectProperties twitterConnectProperties;

    @Bean("TwitterConfiguration")
    public Authentication createAuthentication() {
        return new OAuth1(
                "fXHx28MPFQdM5T4ZHBLhRkiPa",
                "vQvMDgI8Y487fr2nmtWNg3WHsyqm3CXraED8UswUlxibeUIOUM",
                "1466736465950527489-aGlr8N3HkMQUlw1Y9NIePdwPQ31ZOx",
                "DJgE2DuGJjdPUpKMqiKMKVUhM0n4OcDO6NaY9NrUS3rl2");
    }
}
