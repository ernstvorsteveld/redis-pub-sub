package com.frozendevs.redis.pubsub;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.integration.Message;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisQueueConfiguration {

    @Bean
    public Publisher publisher(
            @Qualifier("redisTemplate") RedisTemplate<String, Message<?>> redisTemplate,
            @Qualifier("channelTopic") ChannelTopic channelTopic) {
        return new PublisherRedis(redisTemplate, channelTopic);
    }

    @Bean
    public MessageStore messageStore() {
        return new SimpleMessageStore();
    }

    @Bean
    public MessageListener messageListener(
            @Qualifier("messageStore") MessageStore messageStore,
            @Qualifier("jacksonJsonRedisSerializer") JacksonJsonRedisSerializer jacksonJsonRedisSerializer) {
        return new SubscriberRedis(messageStore, jacksonJsonRedisSerializer);
    }

    @Bean
    public RedisTemplate<String, Message<?>> redisTemplate(
            @Qualifier("jacksonJsonRedisSerializer") JacksonJsonRedisSerializer jacksonJsonRedisSerializer) {
        RedisTemplate<String, Message<?>> redisTemplate = new RedisTemplate<String, Message<?>>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        redisTemplate.setValueSerializer(jacksonJsonRedisSerializer);
        return redisTemplate;
    }

    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(new JedisPoolConfig());
        jedisConnectionFactory.setHostName("localhost");
        jedisConnectionFactory.setPort(6379);
        jedisConnectionFactory.setPassword("");
        jedisConnectionFactory.afterPropertiesSet();
        return jedisConnectionFactory;
    }

    @Bean
    public ChannelTopic channelTopic() {
        return new ChannelTopic("pubsub:channel");
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(
            @Qualifier("messageListener") MessageListener messageListener,
            @Qualifier("jacksonJsonRedisSerializer") JacksonJsonRedisSerializer jacksonJsonRedisSerializer) {
        final MessageListenerAdapter messageListenerAdapter = new MessageListenerAdapter(messageListener);
        messageListenerAdapter.setSerializer(jacksonJsonRedisSerializer);
        return messageListenerAdapter;
    }

    @Bean
    public JacksonJsonRedisSerializer jacksonJsonRedisSerializer() {
        return new JacksonJsonRedisSerializer(SimpleMessage.class);
    }

    @Bean
    public RedisMessageListenerContainer redisMessageListenerContainer(
            @Qualifier("messageListenerAdapter") MessageListenerAdapter messageListenerAdapter,
            @Qualifier("channelTopic") ChannelTopic channelTopic) {
        final RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory());
        container.addMessageListener(messageListenerAdapter, channelTopic);
        return container;
    }
}
