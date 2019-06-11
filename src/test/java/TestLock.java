import org.asyou.redis.base.RedisManager;
import org.asyou.redis.dao.IRedisAdapter;
import org.asyou.redis.dao.RedisAdapter;
import org.asyou.redis.lock.ILock;
import org.asyou.redis.lock.RedisLock;
import org.asyou.redis.time.LocalTimeClient;
import org.asyou.redis.util.IdConfig;
import org.asyou.redis.util.IdGenerator;
import org.asyou.redis.util.IDGeneratorDelegate;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by steven on 17/6/28.
 */
public class TestLock {
    public static IRedisAdapter adapter;
    @Before
    public void Init_before() throws Exception {
        RedisManager.putRedisConfig(new CacheConfig());
        adapter = new RedisAdapter("tuhao-cache");
        atomicInteger = new AtomicInteger(Integer.parseInt(adapter.get("a")));
    }

    @Test
    public void Test_IDG() throws Exception {
        int count=0;
        for(int i=0;i<1000;i++){
            count++;
//            ILock lock1 = new RedisLock(adapter, "lock:lock", 5*1000, new LocalTimeClient());
//            IdGenerator g1 = new IdGenerator(adapter,lock1,new IdConfig("userid",0,1));
//            IDGeneratorTest.IDConsumeTask consume1 = new IDGeneratorTest.IDConsumeTask(g1, "consume" + count);


            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //ID生成器构造依赖
                        ILock lock1 = new RedisLock(adapter, "Lock:lock", 5 * 1000, new LocalTimeClient());
                        IdConfig config = new IdConfig("IdKey:userid", 0, 1);
                        Delegate delegate = new Delegate();

                        //构造ID生成器
                        IdGenerator idGenerator = new IdGenerator(adapter, lock1, config, null);
                        System.err.println(Thread.activeCount() + " : " + idGenerator.getAndIncrement());

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            t.start();
        }
        Thread.sleep(20 * 1000);
    }

    volatile ILock s_lock1;
    volatile IdConfig s_config;
    volatile Delegate s_delegate;
    volatile IdGenerator s_idg;

    public static volatile AtomicInteger atomicInteger;

    public static void add(){
        String last = adapter.getSet("a",atomicInteger.getAndIncrement()+"");
        System.out.println("[" + Thread.currentThread().getName() + "]: " + last + " -> " + String.valueOf(Long.parseLong(last)+1));
    }

    @Test
    public void Test_Thread_Single() throws Exception {
        //ID生成器构造依赖
        s_lock1 = new RedisLock(adapter, "lock:userid", 5*1000, new LocalTimeClient());
        s_config = new IdConfig("id:userid",0,1);
        s_delegate = new Delegate();

        //构造ID生成器
        s_idg = new IdGenerator(adapter,s_lock1,s_config, null);

        for(int i=0;i < 10; i++) { for(int j=0;j<50; j++) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        System.out.println(Thread.activeCount() + " : " + s_idg.getAndIncrement());
                        System.out.println(Thread.activeCount() + " : " + adapter.getSet("a",atomicInteger.getAndIncrement()+""));
//                        add();
                    }
                });
                t.start();
            }
        }

        while(Thread.activeCount() > 2){}
    }

    @Test
    public void Test_Thread(){
        for(int i=0;i<10;i++){ for(int j=0;j<50;j++) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        add();
//                        try {
//                            ILock lock1 = new RedisLock(adapter, "lock:userid", 5 * 1000, new LocalTimeClient());
//                            IdConfig config = new IdConfig("id:userid", 0, 1);
//                            IdGenerator idg = new IdGenerator(adapter, lock1, config, null);
//                            System.out.println(String.format("%s : %s",
//                                    Thread.currentThread().getName(),
//                                    idg.getAndIncrement()));
//                            idg.release();
//                        }catch (Exception e) {
//                            e.printStackTrace();
//                        }
                    }
                });
                t.start();
            }
        }

        while(Thread.activeCount() > 2){}
    }

    @Test
    public void Test_Generator() throws Exception {

        ILock lock1 = new RedisLock(adapter, "lock:userid", 5 * 1000, new LocalTimeClient());

        IdConfig config = new IdConfig("id:userid", 10, 2);

        IDGeneratorDelegate delegate = new Delegate();

        IdGenerator idg = new IdGenerator(adapter, lock1, config, delegate);

        System.out.println(idg.getAndIncrement());

        idg.release();
    }
}
