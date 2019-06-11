package org.asyou.redis.lock;


import org.apache.commons.lang3.StringUtils;
import org.asyou.redis.dao.IRedisAdapter;
import org.asyou.redis.time.ITimeClient;
import redis.clients.jedis.Jedis;

import java.util.concurrent.TimeUnit;

/**
 * 基于Redis的SETNX操作实现的分布式锁
 *
 * 获取锁时最好用lock(long time, TimeUnit unit), 以免网路问题而导致线程一直阻塞
 *
 * <a href="http://redis.io/commands/setnx">SETNX操作参考资料</a>
 *
 */
public final class RedisLock extends AbstractLock {

    private IRedisAdapter adapter;

    private Jedis jedis;

    private ITimeClient timeClient;

    // 锁的名字
    protected String lockKey;

    // 锁的有效时长(毫秒)
    protected long lockExpires;

    /**
     * 构造函数
     * @param adapter Redis 适配器
     * @param lockKey 锁的key字符串
     * @param lockExpires 锁的过期时间
     * @param timeClient 时间获取器
     * @throws Exception 异常信息
     */
    public RedisLock(IRedisAdapter adapter, String lockKey, long lockExpires, ITimeClient timeClient) throws Exception {
        if (adapter == null) {
            throw new Exception("RedisAdapter is null");
        }
        this.adapter = adapter;
        this.jedis = adapter.getHost().getRedis();
        this.lockKey = lockKey;
        this.lockExpires = lockExpires;
        this.timeClient = timeClient;
    }

    /**
     * 阻塞式获取锁，加可中断开关
     * @param useTimeout 是否启用超时监测
     * @param time
     * @param unit
     * @param interrupt 是否响应中断
     * @return 成功获取到锁则返回true，否则返回false
     * @throws InterruptedException 中断异常
     */
    @Override
    protected boolean lock(boolean useTimeout, long time, TimeUnit unit, boolean interrupt) throws InterruptedException{
        if (interrupt) {
            checkInterruption();
        }

        // 超时控制 的时间可以从本地获取, 因为这个和锁超时没有关系, 只是一段时间区间的控制
        long start = localTimeMillis();
        // if !useTimeout, then it's useless
        long timeout = unit.toMillis(time);

        while (useTimeout ? !isTimeout(start, timeout) : true) {
            if (interrupt) {
                checkInterruption();
            }

            //锁超时时间
            long lockExpireTime = serverTimeMillis() + lockExpires + 1;
            String stringOfLockExpireTime = String.valueOf(lockExpireTime);

            // 获取到锁
            if (jedis.setnx(lockKey, stringOfLockExpireTime) == 1) {
                // TODO 成功获取到锁, 设置相关标识
                locked = true;
                setExclusiveOwnerThread(Thread.currentThread());
//                System.err.println(Thread.currentThread().getName() + " : " + stringOfLockExpireTime);
                return true;
            }

            String value = jedis.get(lockKey);
            // lock is expired
            if (value != null && isTimeExpired(value)) {
                // 假设多个线程(非单jvm)同时走到这里
                // getset is atomic
                String oldValue = jedis.getSet(lockKey, stringOfLockExpireTime);
                // 但是走到这里时每个线程拿到的oldValue肯定不可能一样(因为getset是原子性的)
                // 加入拿到的oldValue依然是expired的，那么就说明拿到锁了
                if (oldValue != null && isTimeExpired(oldValue)) {
                    // TODO 成功获取到锁, 设置相关标识
                    locked = true;
                    setExclusiveOwnerThread(Thread.currentThread());
//                    System.err.println(Thread.currentThread().getName() + " : " + stringOfLockExpireTime);
                    return true;
                }
            } else {
                // TODO lock is not expired, enter next loop retrying
            }
        }
        locked = false;
        return false;
    }

    /**
     * 非阻塞获取锁
     * @return 成功获取到锁则返回true，否则返回false
     */
    @Override
    public boolean tryLock() {
        //锁超时时间
        long lockExpireTime = serverTimeMillis() + lockExpires + 1;
        String stringOfLockExpireTime = String.valueOf(lockExpireTime);

        // 获取到锁
        if (jedis.setnx(lockKey, stringOfLockExpireTime) == 1) {
            // TODO 成功获取到锁, 设置相关标识
            locked = true;
            setExclusiveOwnerThread(Thread.currentThread());
            return true;
        }

        String value = jedis.get(lockKey);
        // lock is expired
        if (value != null && isTimeExpired(value)) {
            // 假设多个线程(非单jvm)同时走到这里
            // getset is atomic
            String oldValue = jedis.getSet(lockKey, stringOfLockExpireTime);
            // 但是走到这里时每个线程拿到的oldValue肯定不可能一样(因为getset是原子性的)
            // 假如拿到的oldValue依然是expired的，那么就说明拿到锁了
            if (oldValue != null && isTimeExpired(oldValue)) {
                // TODO 成功获取到锁, 设置相关标识
                locked = true;
                setExclusiveOwnerThread(Thread.currentThread());
                return true;
            }
        } else {
            // TODO lock is not expired, enter next loop retrying
        }
        locked = false;
        return false;
    }

    /**
     * 获取锁使用的key
     * @return 返回key
     */
    @Override
    public String getLockKey() {
        return lockKey;
    }

    /**
     * Queries if this lock is held by any thread.
     *
     * @return {@code true} if any thread holds this lock and
     *         {@code false} otherwise
     */
    public boolean isLocked() {
        if (locked) {
            return true;
        } else {
            String value = jedis.get(lockKey);
            // TODO 这里其实是有问题的, 想:当get方法返回value后, 假设这个value已经是过期的了,
            // 而就在这瞬间, 另一个节点set了value, 这时锁是被别的线程(节点持有), 而接下来的判断
            // 是检测不出这种情况的.不过这个问题应该不会导致其它的问题出现, 因为这个方法的目的本来就
            // 不是同步控制, 它只是一种锁状态的报告.
            return !isTimeExpired(value);
        }
    }

    @Override
    protected synchronized void unlock0() {
        // TODO 判断锁是否过期
        String value = jedis.get(lockKey);
        if (StringUtils.isNotBlank(value) && !isTimeExpired(value)) {
            doUnlock();
            locked = false;
        }
    }

    @Override
    public synchronized void release() {
        adapter.release(jedis);
    }

    private synchronized void doUnlock() {
        jedis.del(lockKey);
    }

    private void checkInterruption() throws InterruptedException {
        if(Thread.currentThread().isInterrupted()) {
            throw new InterruptedException();
        }
    }

    private boolean isTimeExpired(String value) {
        // 这里拿服务器的时间来比较
        return Long.parseLong(value) < serverTimeMillis();
    }

    private boolean isTimeout(long start, long timeout) {
        // 这里拿本地的时间来比较
        return start + timeout < System.currentTimeMillis();
    }

    private long serverTimeMillis(){
        return timeClient.getTime();
    }

    private long localTimeMillis() {
        return System.currentTimeMillis();
    }

}