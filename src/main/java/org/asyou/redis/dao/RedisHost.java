package org.asyou.redis.dao;

import org.apache.commons.lang3.StringUtils;
import java.util.logging.Logger;

import org.asyou.redis.base.RedisConfig;
import org.asyou.redis.base.RedisManager;
import org.asyou.redis.exception.CodeException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.Map;

/**
 * Redis服务器管理器，用于管理全局所有的Redis服务器
 * Created by steven on 2016/9/27.
 */
public final class RedisHost implements IRedisHost{

    private final static Logger log = Logger.getLogger(RedisHost.class.getName());

    private RedisConfig config;
    private JedisPool jedisPool;

    private RedisHost() { }

    public RedisHost(RedisConfig redisConfig) throws Exception {
        if (redisConfig.getJedisPoolConfig() == null) {
            throw new Exception("RedisConfig JedisPoolConfig is null");
        }
        config = redisConfig;
        if (StringUtils.isNotBlank(config.getPassword())) {
            jedisPool = new JedisPool(config.getJedisPoolConfig(), redisConfig.getHostName(), redisConfig.getPort(), 60000, config.getPassword());
        } else {
            jedisPool = new JedisPool(config.getJedisPoolConfig(), redisConfig.getHostName(), redisConfig.getPort(), 60000);
        }
    }

    public RedisConfig getConfig(){
        return this.config;
    }

    public synchronized Jedis getRedis(){
        return jedisPool.getResource();
    }

    public void releaseJedis(Jedis jedis){
        if (jedis != null) {
            String str = "jedis: ";
            try {
                jedis.close();
//                jedis.disconnect();
//                jedis.quit();
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
//                StackTraceElement stack[] = Thread.currentThread().getStackTrace();
//                for (StackTraceElement ste:stack){
//                    sb.append(ste.getClassName()+" -> ");
//                }
                log.info(String.format("Jedis close error / level: %s / %s / %s", sb.toString(),e.getMessage(),str));
            }
        }
    }

    public synchronized void releaseHost(){
        if (jedisPool != null && !jedisPool.isClosed()) {
            log.info(String.format("RedisHost '%s' release.",config.getId()));
            jedisPool.close();
        }
    }

    public String hostInfo(){
        return hostInfo(RedisInfo.INFO_CLIENTS);
    }

    public String hostInfo(String section){
        String value = "";
        Jedis redis = null;
        try {
            redis = getRedis();
            value = redis.info(section);
        } catch (Exception e) {
            //释放redis对象
            log.info("id: '" + config.getId() + "' " + e.getMessage());
            releaseJedis(redis);
        } finally {
            //返还到连接池
            releaseJedis(redis);
        }
        return value;
    }

    @Override
    public String toString(){
        return "["+RedisHost.class.getSimpleName()+"] " + config;
    }

    public synchronized static void releaseAllHost(){
        Map<String,RedisHost> map = RedisManager.getHosts();
        for (Map.Entry<String,RedisHost> entry : map.entrySet()){
            entry.getValue().releaseHost();
        }
    }
}
