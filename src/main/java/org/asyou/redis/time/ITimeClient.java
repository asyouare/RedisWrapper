package org.asyou.redis.time;

import java.util.Date;

/**
 * 时间获取器
 * Created by steven on 2016/12/5.
 */
public interface ITimeClient {
    Date getDate();
    long getTime();
}
