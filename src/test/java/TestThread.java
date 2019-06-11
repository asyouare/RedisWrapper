import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by steven on 17/7/12.
 */
public class TestThread extends TestBaseInit{
    public static TestThread o = new TestThread();
    public static void notifyFunc(){
        synchronized (o){
            o.notifyAll();
        }
    }
    public static void SyncFunc(String str){
        synchronized (o) {
            try {
                o.wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }
                System.out.println(str + " - SyncFunc - " + Thread.currentThread().getName());
            }
        }
    }

    @Test
    public void Test_Thread1() throws InterruptedException {
        int count = 0;
        long startTime = System.nanoTime();
        for(int i=0;i<200;i++){
            int a = count++;
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    Jedis jedis = adapter.getRedis();
                    System.out.println(adapter.getHost().hostInfo() + "Thread ID: " + Thread.currentThread().getId() + " , count: " + a + "\r\n");
                    try {
                        Thread.sleep(2 * 1000);
                    } catch (Exception e) {
                    }
                    jedis.close();
                }
            });
            t.start();
        }


        while(Thread.activeCount() > 3){}

        Thread.sleep(2 * 1000);

        System.err.println("-----------");
        for(int i=0;i<40;i++) {
            Jedis jedis1 = adapter.getRedis();
        }
        System.err.println(adapter.getHost().hostInfo() + "Thread ID: " + Thread.currentThread().getId() + " , count: " + count + "\r\n");
        System.err.println("-----------");

        System.out.println(adapter.getHost().hostInfo() + "Thread count: " + Thread.activeCount() + " , count: " + count + "\r\n");
        System.err.println(String.format("use time: %s",(System.nanoTime()-startTime)/1000/1000));

        Thread.sleep(60 * 1000);

    }

}
