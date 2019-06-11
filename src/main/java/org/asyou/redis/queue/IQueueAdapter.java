package org.asyou.redis.queue;

/**
 * Created by steven on 2016/12/6.
 */
public interface IQueueAdapter {
    void push(byte[] key, byte[] value);
    byte[] pop(byte[] key);
}
