import org.asyou.redis.base.RedisConfig;
import org.asyou.redis.base.RedisManager;
import org.asyou.redis.dao.IRedisAdapter;
import org.asyou.redis.dao.RedisAdapter;
import org.asyou.redis.lock.ILock;
import org.asyou.redis.lock.RedisLock;
import org.asyou.redis.time.ITimeClient;
import org.asyou.redis.time.LocalTimeClient;
import org.asyou.redis.util.IdConfig;
import org.asyou.redis.util.IdGenerator;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

public class IDGeneratorTest {

    private static Set<String> generatedIds = new HashSet<String>();

    private static final String LOCK_KEY = "lock.lock";
    private static final long LOCK_EXPIRE = 5 * 1000;

    @Test
    public void testV1_0() throws Exception {

//        SocketAddress addr = new InetSocketAddress("localhost", 9091);
        RedisConfig config = RedisConfig.build().build().setId("tuhao-cache").setHostName("47.93.160.185").setPort(6379);
        RedisManager.putRedisConfig(config);

        IRedisAdapter timeAdapter = new RedisAdapter("tuhao-cache");
        ITimeClient timeClient = new LocalTimeClient();

        IRedisAdapter lockAdapter = RedisAdapter.selectHost("tuhao-cache");

        ILock lock1 = new RedisLock(lockAdapter, "lock:user", LOCK_EXPIRE, timeClient);
        IdGenerator g1 = new IdGenerator(lockAdapter,lock1,new IdConfig("userid",0,1),null);
        IDConsumeTask consume1 = new IDConsumeTask(g1, "consume1");

        ILock lock2 = new RedisLock(lockAdapter, "lock:order", LOCK_EXPIRE, timeClient);
        IdGenerator g2 = new IdGenerator(lockAdapter,lock2,new IdConfig("orderid",0,2),null);
        IDConsumeTask consume2 = new IDConsumeTask(g2, "consume2");

        Thread t1 = new Thread(consume1);
        Thread t2 = new Thread(consume2);
        t1.start();
        t2.start();

        Thread.sleep(20 * 1000); //让两个线程跑20秒

        IDConsumeTask.stopAll();

        t1.join();
        t2.join();
    }

    static String time() {
        return String.valueOf(System.currentTimeMillis() / 1000);
    }

    static class IDConsumeTask implements Runnable {

        private IdGenerator idGenerator;

        private String name;

        private static volatile boolean stop;

        public IDConsumeTask(IdGenerator idGenerator, String name) {
            this.idGenerator = idGenerator;
            this.name = name;
        }

        public static void stopAll() {
            stop = true;
        }

        public void run() {
//            System.out.println(time() + ": consume " + name + " start ");
            while (!stop) {
                String id = idGenerator.getAndIncrement();
                if (id != null) {
//                    if(generatedIds.contains(id)) {
//                        System.out.println(time() + ": duplicate id generated, id = " + id);
//                        stop = true;
//                        continue;
//                    }

//                    generatedIds.add(id);
                    System.out.println(time() + ": consume " + name + " add id = " + id);
                }
            }
            // 释放资源
            idGenerator.release();
            System.out.println(time() + ": consume " + name + " done ");
        }

    }

}