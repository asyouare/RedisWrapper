package org.asyou.redis.util;

/**
 * Created by steven on 17/6/30.
 */
public interface IDGeneratorDelegate {
    void doCallback(Object... args);
    void doCallback();
}
