package com.frozendevs.redis.pubsub;

import com.google.common.collect.Maps;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.integration.Message;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RedisQueueConfiguration.class})
public class PubSubITest {

    @Autowired
    private Publisher publisher;

    @Autowired
    private MessageStore messageStore;

    @Autowired
    private RedisTemplate<String,Message> redisTemplate;

    @Test
    public void should_publish_and_subscribe() throws InterruptedException {
        Map<String, String> headers = expectHeaders();
        final Message<String> message = MessageBuilder.withPayload("Message.1").copyHeaders(headers).build();
        publisher.publish(message);
        Thread.sleep(1000);
        assertThat(messageStore.readAll().size(), is(1));
        assertThat((String) messageStore.readAll().get(0).getHeaders().get("key.1"), is("value.1"));
        assertThat((String) messageStore.readAll().get(0).getPayload(), is("Message.1"));

    }

    private Map<String, String> expectHeaders() {
        Map<String, String> headers = Maps.newHashMap();
        headers.put("key.1", "value.1");
        return headers;
    }
}
