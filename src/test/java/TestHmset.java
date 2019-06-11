import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by steven on 17/7/10.
 */
public class TestHmset extends TestBaseInit{
    @Test
    public void Test_Hset(){
//        Jedis jedis = adapter.getRedis();
        adapter.set("a", "1");
        System.out.println(adapter.get("a"));
        
    }

    @Test
    public void Test_ManyConnect() throws InterruptedException {
        Jedis jedis = adapter.getRedis();

        new Thread(new Runnable() {
            @Override
            public void run() {
                adapter.getRedis();
            }
        }).start();
        Thread.sleep(900);
        jedis.close();
        Thread.sleep(2000);
    }
}
