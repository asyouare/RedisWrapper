package org.asyou.redis.queue;

import org.asyou.redis.dao.IRedisAdapter;

/**
 * Created by steven on 2016/12/5.
 */
public class QueueAdapter implements IQueueAdapter {

    private IRedisAdapter adapter;
//    private String queueKey;
//    private String lockKey;

    public QueueAdapter(IRedisAdapter adapter){
        this.adapter = adapter;
    }

    @Override
    public void push(byte[] key, byte[] value){
        if (this.adapter != null) {
            this.adapter.lpush(key, value);
        }
    }

    @Override
    public byte[] pop(byte[] key){
        return this.adapter != null ? this.adapter.rpop(key) : null;
    }

    public <T> void push(String key, T value){

    }

//    public <T> T pop(String key) {
//
//    }
}
