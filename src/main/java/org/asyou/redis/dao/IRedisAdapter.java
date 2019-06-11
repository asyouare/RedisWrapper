package org.asyou.redis.dao;

import redis.clients.jedis.Jedis;

import java.io.Serializable;

/**
 * Created by steven on 2016/12/5.
 */
public interface IRedisAdapter extends Cloneable,Serializable {
    RedisHost getHost();
    Jedis getRedis();

    void release(Jedis jedis);
    void releaseHost();

    boolean exists(String key);
    boolean exists(byte[] key);

    String get(String key);
    void set(String key, String value);
    void set(String key, String value, int time);

    byte[] get(byte[] key);
    void set(byte[] key, byte[] value);
    void set(byte[] key, byte[] value, int time);

    String getSet(String key, String value);

    void lpush(byte[] key, byte[] value);
    void lpush(String key, String value);
    byte[] rpop(byte[] key);
    String rpop(String key);

    void del(byte[] key);
    void del(String key);
}
