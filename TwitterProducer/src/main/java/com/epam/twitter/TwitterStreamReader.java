package com.epam.twitter;

import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.Iterator;
import java.util.Properties;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;


public class TwitterStreamReader {

    private Properties properties;
    private BlockingQueue<String> msgQueue = new LinkedBlockingQueue<>(10000);
    private BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<>(1000);
    private BasicClient client;
    @Autowired
    @Qualifier("TwitterConfiguration")
    private Authentication authentication;


    public TwitterStreamReader() {
        init();
    }

    private void init() {
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
        client = new ClientBuilder()
                .authentication(authentication)
                .hosts(hosebirdHosts)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue)
                .build();
        client.connect();
    }

    public Iterator<String> messageIterator() {
        return new MessageIterator(client, msgQueue);
    }

    public Iterator<Event> eventIterator() {
        return new EventIterator(client, eventQueue);
    }

    private static class MessageIterator implements Iterator<String> {

        private final BasicClient client;
        private final BlockingQueue<String> msgQueue;

        public MessageIterator(BasicClient client, BlockingQueue<String> msgQueue) {
            this.client = client;
            this.msgQueue = msgQueue;
        }

        @Override
        public boolean hasNext() {
            return !client.isDone();
        }

        @Override
        public String next() {
            try {
                return msgQueue.take();
            } catch (InterruptedException e) {
                System.out.println(e);
                throw new RuntimeException();
            }
        }
    }

    private static class EventIterator implements Iterator<Event> {

        private final BasicClient client;
        private final BlockingQueue<Event> eventQueue;

        public EventIterator(BasicClient client, BlockingQueue<Event> eventQueue) {
            this.client = client;
            this.eventQueue = eventQueue;
        }

        @Override
        public boolean hasNext() {
            return !client.isDone();
        }

        @Override
        public Event next() {
            try {
                return eventQueue.take();
            } catch (InterruptedException e) {
                System.out.println(e);
                throw new RuntimeException();
            }
        }
    }
}