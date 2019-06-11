package org.asyou.redis.time;

import java.util.Date;

/**
 * 本地时间
 * Created by steven on 2016/12/5.
 */
public class LocalTimeClient implements ITimeClient{

    public LocalTimeClient(){ }

    @Override
    public Date getDate(){
        return new Date();
    }

    @Override
    public long getTime(){
        return System.currentTimeMillis();
    }
}
