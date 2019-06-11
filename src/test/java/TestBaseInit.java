import org.asyou.redis.base.RedisManager;
import org.asyou.redis.dao.IRedisAdapter;
import org.asyou.redis.dao.RedisAdapter;
import org.junit.Before;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by steven on 17/7/10.
 */
public class TestBaseInit {
    public RedisAdapter adapter;
    @Before
    public void Init_before() throws Exception {
        RedisManager.putRedisConfig(new CacheConfig());
        adapter = new RedisAdapter("tuhao-cache");
    }
}
