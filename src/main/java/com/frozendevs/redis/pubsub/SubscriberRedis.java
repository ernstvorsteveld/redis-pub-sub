package com.frozendevs.redis.pubsub;

import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.integration.support.MessageBuilder;

public class SubscriberRedis implements MessageListener {

    private final MessageStore messageStore;
    private final JacksonJsonRedisSerializer jacksonJsonRedisSerializer;

    public SubscriberRedis(MessageStore messageStore, JacksonJsonRedisSerializer jacksonJsonRedisSerializer) {
        this.messageStore = messageStore;
        this.jacksonJsonRedisSerializer = jacksonJsonRedisSerializer;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {
        final byte[] body = message.getBody();
        SimpleMessage simpleMessage = (SimpleMessage) jacksonJsonRedisSerializer.deserialize(body);

        org.springframework.integration.Message<?> message2 = MessageBuilder
                .withPayload(simpleMessage.getT())
                .copyHeaders(simpleMessage.getHeaders())
                .build();
        messageStore.store(message2);
    }
}
