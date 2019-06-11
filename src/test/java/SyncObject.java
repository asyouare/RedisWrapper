import org.junit.Test;

/**
 * Created by steven on 17/7/12.
 */
public class SyncObject {
//    public static volatile int count = 0;

    public void talk(String str){
        synchronized (o) {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(48);
                } catch (Exception e) {
                }
                System.out.println(str + " - talk - " + Thread.currentThread().getName());
            }
        }
    }

    public void inc(String str){
        synchronized (o){
            for(int i=0;i<10;i++) {
                try {
                    Thread.sleep(50);
                } catch (Exception e) {
                }
                System.out.println(str + " - inc - " + Thread.currentThread().getName());
            }
        }
    }

    public void sync(String str){
        synchronized (o){
            while(count > 0) {
                count--;
                try {
                    o.wait();
                } catch (Exception e) {
                }
            }
            System.out.println(str + " - sync - " + Thread.currentThread().getName());
            o.notifyAll();
        }
    }

    public static volatile SyncObject o = new SyncObject();

    public static volatile int count = 1;

    @Test
    public void Test_Thread(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                TestThread.SyncFunc("3");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                o.sync("1");

            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                o.sync("2");
//                TestThread.SyncFunc("3");
            }
        }).start();

        TestThread.notifyFunc();

        while (Thread.activeCount() > 2){}


    }
}
