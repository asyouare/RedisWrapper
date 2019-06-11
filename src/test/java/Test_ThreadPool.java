//import org.asyou.redis.exception.CodeException;
//import org.asyou.redis.lock.ILock;
//import org.asyou.redis.lock.RedisLock;
//import org.asyou.redis.time.LocalTimeClient;
//import org.junit.Test;
//import pro.tools.system.ToolThreadPool;
//
//import java.util.Arrays;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by steven on 17/8/1.
// */
//public class Test_ThreadPool  extends  TestBaseInit{
//    int count = 0;
//    @Test
//    public void testLock() throws Exception {
//        ToolThreadPool threadPool = new ToolThreadPool(ToolThreadPool.Type.CachedThread, 100);
//        for (int i = 0; i++ < 100; ) {
//            int finalI = i;
//            threadPool.submit(() -> {
//                try {
//                    doSomething(finalI);
//                } catch (Exception e) {
//                    System.out.println(Thread.currentThread().getName()+" : "+e.getMessage());
//                }
//            });
//        }
////        while (count < 102) ;
//
//        while(true);
//
////        for(int i=0;i<100;i++){
////            new Thread(new Runnable() {
////                @Override
////                public void run() {
////                    try {
////                        doSomething();
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                }
////            }).start();
////        }
////        while(Thread.activeCount() > 2);
//
////        System.err.println("Count: " + count);
//    }
//
//    public void doSomething(int i) throws Exception {
//        ILock lock = new RedisLock(adapter, "lock:loan:1", 5 * 1000, new LocalTimeClient());
//        if (lock.tryLock(3 * 1000, TimeUnit.MILLISECONDS)) {
//            try {
//                System.out.println(count + "/" + i + " do something. " + adapter.get("lock:loan:1"));
//                Thread.sleep(2 * 1000);
////                System.out.println(Thread.currentThread().getName() + " : " + adapter.get("lock:loan:1"));
//                count ++;
//            } catch (Exception e) {
//                System.out.println(CodeException.getExceptionString(e));
//            } finally {
//                lock.unlock();
//            }
//        }else{
//            System.err.println("not get key");
//        }
//    }
//}
