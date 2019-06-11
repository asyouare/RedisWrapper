package org.asyou.redis.queue;

import java.io.Serializable;

/**
 * Created by steven on 2016/12/2.
 */
public class QueueConfig implements Cloneable, Serializable {

    /**
     * 克隆对象
     * @return 返回复制品
     */
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
