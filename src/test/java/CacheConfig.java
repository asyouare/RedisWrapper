import org.asyou.redis.base.RedisConfig;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by steven on 17/6/16.
 */
public class CacheConfig extends RedisConfig {
    public CacheConfig(){
//        super("tuhao-cache", "192.168.15.100", 6379);
        super("tuhao-cache", "47.93.160.185", 6379);
//        super("tuhao-cache", "localhost", 6379);
//        JedisPoolConfig poolConfig = createJedisPoolConfig();
//        poolConfig.setMaxTotal(1);
//        poolConfig.setMaxIdle(1);
//        poolConfig.setMaxWaitMillis(1000);
//        init(
//            "tuhao-cache",
//            "localhost",//"47.93.160.185",
//            6379,
//            null,
//            null,
//            poolConfig
//        );
    }
}
