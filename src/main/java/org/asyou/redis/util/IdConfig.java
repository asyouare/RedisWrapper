package org.asyou.redis.util;

import java.io.Serializable;

/**
 * Created by steven on 17/6/30.
 */
public class IdConfig implements Cloneable,Serializable {

    public IdConfig(){

    }

    public IdConfig(String idKey, long idInit, int increment){
        this.idKey = idKey;
        this.idInit = idInit;
        this.increment = increment;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    private String idKey;
    private long idInit;
    private int increment;
    private long timeout = 5;


    public String getIdKey() {
        return idKey;
    }

    public void setIdKey(String idKey) {
        this.idKey = idKey;
    }

    public long getIdInit() {
        return idInit;
    }

    public void setIdInit(long idInit) {
        this.idInit = idInit;
    }

    public int getIncrement() {
        return increment;
    }

    public void setIncrement(int increment) {
        this.increment = increment;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}
