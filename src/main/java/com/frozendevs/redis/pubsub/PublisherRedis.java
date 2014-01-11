package com.frozendevs.redis.pubsub;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.integration.Message;

public class PublisherRedis implements Publisher {

    private final RedisTemplate<String, Message<?>> redisTemplate;
    private final ChannelTopic channelTopic;

    public PublisherRedis(RedisTemplate<String, Message<?>> redisTemplate, ChannelTopic channelTopic) {
        this.redisTemplate = redisTemplate;
        this.channelTopic = channelTopic;
    }

    @Override
    public void publish(Message<?> message) {
        SimpleMessage simpleMessage = new SimpleMessage();
        simpleMessage.setHeaders(message.getHeaders());
        simpleMessage.setT(message.getPayload());
        this.redisTemplate.convertAndSend(channelTopic.getTopic(), simpleMessage);
    }
}
