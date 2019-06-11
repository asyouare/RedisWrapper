package org.asyou.redis.dao;

import org.asyou.redis.base.RedisManager;
import redis.clients.jedis.Jedis;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

/**
 * Redis适配器，用于快速进行CRUD操作
 * Created by steven on 2016/9/27.
 */
public final class RedisAdapter implements IRedisAdapter {
    

    //region <静态属性>
    private final static Logger log = Logger.getLogger(RedisAdapter.class.getName());

    public static RedisAdapter selectHost(String id) throws Exception {
        return new RedisAdapter(id);
    }

    public static RedisHost getHost(String id) throws Exception {
        return RedisManager.getRedisHost(id);
    }

    public static void releaseAllHost(){
        RedisHost.releaseAllHost();
    }

    public static int hostSize(){
        return RedisManager.getSize();
    }

    //endregion

    private String id;
    private RedisHost host = null;

    public RedisAdapter(String id) throws Exception {
        this.id = id;
        host = RedisManager.getRedisHost(this.id);
    }

    @Override
    public RedisHost getHost(){
        return host;
    }

    @Override
    public Jedis getRedis(){
        return host.getRedis();
    }

    @Override
    public void release(Jedis jedis){
        host.releaseJedis(jedis);
    }

    @Override
    public synchronized void releaseHost(){
        host.releaseHost();
    }

    public String hostInfo(String section){
        return host.hostInfo(section);
    }

    public String hostInfo(){
        return host.hostInfo();
    }


    /**
     * 判断key是否存在
     */
    @Override
    public boolean exists(String key) {
        boolean value = false;
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.exists(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return value;
    }

    /**
     * 判断key是否存在
     */
    @Override
    public boolean exists(byte[] key) {
        boolean value = false;
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.exists(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return value;
    }


    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {
        String value = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.get(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return value;
    }
    
    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    @Override
    public byte[] get(byte[] key) {
        byte[] value = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.get(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return value;
    }

    @Override
    public void set(String key, String value){
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.set(key, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    @Override
    public void set(String key, String value, int time){
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.set(key, value);
            redis.expire(key, time);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    @Override
    public void set(byte[] key, byte[] value) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.set(key, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    @Override
    public void set(byte[] key, byte[] value, int time) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.set(key, value);
            redis.expire(key, time);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    @Override
    public String getSet(String key, String value){
        Jedis redis = null;
        String result = null;
        try {
            redis = getRedis();
            result = redis.getSet(key, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return result;
    }

    public void hset(byte[] key, byte[] field, byte[] value) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.hset(key, field, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    public void hset(String key, String field, String value) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.hset(key, field, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public String hget(String key, String field) {
        String value = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.hget(key, field);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return value;
    }

    /**
     * 获取数据
     *
     * @param key
     * @return
     */
    public byte[] hget(byte[] key, byte[] field) {
        byte[] value = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.hget(key, field);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return value;
    }

    public void hdel(byte[] key, byte[] field) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.hdel(key, field);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    /**
     * 存储REDIS队列 顺序存储
     *
     * @param key   key reids键名
     * @param value value 键值
     */
    @Override
    public void lpush(byte[] key, byte[] value) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.lpush(key, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    @Override
    public void lpush(String key, String value) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.lpush(key, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    /**
     * 存储REDIS队列 反向存储
     *
     * @param key   reids键名
     * @param value 键值
     */
    public void rpush(byte[] key, byte[] value) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.rpush(key, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    public void rpush(String key, String value) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.rpush(key, value);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    /**
     * 将列表 source 中的最后一个元素(尾元素)弹出，并返回给客户端
     *
     * @param key         reids键名
     * @param destination 键值
     */
    public void rpoplpush(byte[] key, byte[] destination) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.rpoplpush(key, destination);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    /**
     * 获取队列数据
     * @param key 键名
     * @return
     */
    public List<byte[]> lpopList(byte[] key) {
        List<byte[]> list = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            list = redis.lrange(key, 0, -1);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return list;
    }

    /**
     * 获取队列数据
     *
     * @param key 键名
     * @return
     */
    @Override
    public byte[] rpop(byte[] key) {
        byte[] bytes = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            bytes = redis.rpop(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return bytes;
    }

    @Override
    public String rpop(String key) {
        String value = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.rpop(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return value;
    }

    public void hmset(Object key, Map<String, String> hash) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.hmset(key.toString(), hash);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    public void hmset(Object key, Map<String, String> hash, int time) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.hmset(key.toString(), hash);
            redis.expire(key.toString(), time);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    public List<String> hmget(Object key, String... fields) {
        List<String> result = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            result = redis.hmget(key.toString(), fields);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return result;
    }

    public Set<String> hkeys(String key) {
        Set<String> result = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            result = redis.hkeys(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return result;
    }

    public List<byte[]> lrange(byte[] key, int from, int to) {
        List<byte[]> result = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            result = redis.lrange(key, from, to);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return result;
    }

    public Map<byte[], byte[]> hgetAll(byte[] key) {
        Map<byte[], byte[]> result = null;
        Jedis redis = null;
        try {
            redis = getRedis();
            result = redis.hgetAll(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return result;
    }

    @Override
    public void del(byte[] key) {
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.del(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    @Override
    public void del(String key){
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.del(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
    }

    public long llen(byte[] key) {
        long len = 0;
        Jedis redis = null;
        try {
            redis = getRedis();
            redis.llen(key);
        } catch (Exception e) {
            log.info("id: '" + id + "' " + e.getMessage());
        } finally {
            //返还到连接池
            release(redis);
        }
        return len;
    }

    public long size() {
        return getRedis().dbSize();
    }
}
