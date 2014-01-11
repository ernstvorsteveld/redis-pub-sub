package com.frozendevs.redis.pubsub;

import org.springframework.integration.Message;

public interface Publisher {

    void publish(Message<?> message);
}
