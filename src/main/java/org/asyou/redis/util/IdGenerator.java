package org.asyou.redis.util;


import org.asyou.redis.dao.IRedisAdapter;
import org.asyou.redis.exception.CodeException;
import org.asyou.redis.lock.ILock;
import org.asyou.redis.lock.Releasable;

import java.util.logging.Logger;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;


/**
 * 模拟分布式环境中的ID生成
 */
public class IdGenerator implements Releasable {
    private final static Logger log = Logger.getLogger(IdGenerator.class.getName());

    private IRedisAdapter adapter;
    private ILock lock;
    private IdConfig config;
    private IDGeneratorDelegate delegate;

    private BigInteger id;
    private final BigInteger increment;

    public IdGenerator(IRedisAdapter adapter, ILock lock, IdConfig config) throws Exception {
        this(adapter,lock,config, null);
    }

    public IdGenerator(IRedisAdapter adapter, ILock lock, IdConfig config, IDGeneratorDelegate delegate) throws Exception {
        this.adapter = adapter;
        this.lock = lock;
        this.config = config;
        this.delegate = delegate;
        this.id = BigInteger.valueOf(config.getIdInit());
        this.increment = BigInteger.valueOf(config.getIncrement());
    }

    public synchronized String getAndIncrement() {
        //使用超时返回的阻塞锁
        if (lock.tryLock(config.getTimeout(), TimeUnit.SECONDS)) {
            try {
                // TODO 这里获取到锁, 访问临界区资源
//                log.info(Thread.currentThread().getName() + " get lock");
                    return getAndIncrement1();
            } catch (Exception e){
                log.info(CodeException.getExceptionString(e));
            }finally {
                lock.unlock();
            }
        }
        return null;
        //return getAndIncrement0();
    }

    @Override
    public synchronized void release() {
        lock.release();
    }

    private synchronized String getAndIncrement0() {
        String s = id.toString();
        id = id.add(increment);

        adapter.set(config.getIdKey(), id.toString());

        if (delegate != null) {
            delegate.doCallback(s);
        }

        return s;
    }

    private synchronized String getAndIncrement1() {
        String id = adapter.get(config.getIdKey());

//        if (id == null) {
//            id = String.valueOf(config.getIdInit());
//        }
//        long idIncrement =  Long.parseLong(id) + config.getIncrement();

        long idIncrement = id == null ?
                config.getIdInit() : Long.parseLong(id) + config.getIncrement();

        adapter.set(config.getIdKey(), String.valueOf(idIncrement));

        if (delegate != null) {
            delegate.doCallback(id);
        }

        return id;
    }
}