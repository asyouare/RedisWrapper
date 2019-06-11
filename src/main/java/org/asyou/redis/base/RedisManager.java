package org.asyou.redis.base;

import org.apache.commons.lang3.StringUtils;
import org.asyou.redis.dao.RedisHost;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steven on 17/6/15.
 */
public class RedisManager {
    private static Map<String,RedisHost> map = null;

    public static Map<String,RedisHost> getHosts(){return map;}

    public static int getSize(){
        return map != null ? map.size() : 0;
    }

    public static RedisHost getRedisHost(String id) throws Exception {
        if (!map.containsKey(id)) {
            throw new Exception(String.format("RedisAdapter id'%s' is not found", id));
        }
        return map.get(id);
    }

    public static boolean putRedisConfig(RedisConfig... configs) throws Exception {
        if (configs.length > 0) {
            Map<String, RedisHost> cmap = new HashMap();
            for (RedisConfig config : configs) {
                if (config == null) {
                    throw new Exception(String.format("RedisConfig is null"));
                }
                if (StringUtils.isBlank(config.getId())) {
                    throw new Exception(String.format("RedisConfig id is null"));
                }
                if (map.containsKey(config.getId())) {
                    throw new Exception(String.format("RedisConfig id'%s' is already use", config.getId()));
                }

                //config 正确性通过，检查JedisPoolConfig正确性
                if (config.getJedisPoolConfig() == null) {
                    throw new Exception(String.format("JedisPoolConfig is null"));
                }

                cmap.put(config.getId(), new RedisHost(config));
            }

            if (configs.length == cmap.size()){
                map.putAll(cmap);
                return true;
            }else{
                throw new Exception(String.format("RedisConfig setting error"));
            }
        }
        return false;
    }

    public static String getMapString(){
        StringBuilder strb = new StringBuilder();
        for(Map.Entry<String,RedisHost> entry : map.entrySet()){
            strb.append(entry.getValue().getConfig().toString());
            strb.append("\r\n");
        }
        return strb.toString();
    }

    static {
        init();
    }

    private static synchronized void init(){
        if (map == null) {
//            log.info("[" + MongoConfig.class.getSimpleName() + "] init...");
            map = new HashMap<>();
        }
    }
}
