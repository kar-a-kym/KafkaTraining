package com.epam.twitter;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.Hosts;
import com.twitter.hbc.core.HttpHosts;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.event.Event;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.BasicClient;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TwitterReader {

    public static void main(String[] args) {
        BlockingQueue<String> msgQueue = new LinkedBlockingQueue<String>(100000);
        BlockingQueue<Event> eventQueue = new LinkedBlockingQueue<Event>(1000);

/** Declare the host you want to connect to, the endpoint, and authentication (basic auth or oauth) */
        Hosts hosebirdHosts = new HttpHosts(Constants.STREAM_HOST);
        StatusesFilterEndpoint hosebirdEndpoint = new StatusesFilterEndpoint();
// Optional: set up some followings and track terms
        List<Long> followings = Lists.newArrayList(1234L, 566788L);
        List<String> terms = Lists.newArrayList("twitter", "api");
        hosebirdEndpoint.followings(followings);
        hosebirdEndpoint.trackTerms(terms);

        Authentication auth = new OAuth1(
                "7CGwbfw9u45y6RvfQeEEY8HDc",
                "lc0ULV4nQRtfdNyEH31vlx8vRMTAF2z8pVMy7a3i0NZxupBlt0",
                "1466736465950527489-KeOHLpoQNIh0ZwyaY67a5O8YEjVN3Z",
                "oIsEmOdgCOcXBLvNP2RfNOcf13xWlNpyHg06zA7UwdNjW");
        BasicClient client = new ClientBuilder()
                .authentication(auth)
                .hosts(hosebirdHosts)
                .endpoint(hosebirdEndpoint)
                .processor(new StringDelimitedProcessor(msgQueue))
                .eventMessageQueue(eventQueue)
                .build();
        client.connect();


        int i = 0;
        while (true) {
            while(!eventQueue.isEmpty()) {
                System.out.println(eventQueue.poll().getMessage());
            }
            while(!msgQueue.isEmpty()) {
                System.out.println(msgQueue.poll());
            }
        }
    }
}
