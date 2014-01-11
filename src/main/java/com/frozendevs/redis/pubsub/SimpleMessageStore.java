package com.frozendevs.redis.pubsub;

import com.google.common.collect.Lists;
import org.springframework.integration.Message;

import java.util.List;

public class SimpleMessageStore implements MessageStore {

    private List<Message> messageList = Lists.newArrayList();

    @Override
    public <T> void store(Message<T> message) {
        messageList.add(message);
    }

    @Override
    public <T> List<Message> readAll() {
        return messageList;
    }

}
