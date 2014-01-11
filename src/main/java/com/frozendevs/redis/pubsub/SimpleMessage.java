package com.frozendevs.redis.pubsub;

import java.util.Map;

public class SimpleMessage<T> {

    private Map<String, Object> headers;
    private T t;

    public Map<String, Object> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, Object> headers) {
        this.headers = headers;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }
}
