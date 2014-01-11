package com.frozendevs.redis.pubsub;

import org.springframework.integration.Message;

import java.util.List;

public interface MessageStore {

    <T> void store(Message<T> message);

    <T> List<Message> readAll();
}
