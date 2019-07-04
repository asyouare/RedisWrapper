import org.asyou.redis.base.RedisConfig;
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
        RedisConfig config = RedisConfig.build().build().setId("tuhao-cache").setHostName("47.93.160.185").setPort(6379);
        RedisManager.putRedisConfig(config);
        adapter = new RedisAdapter(config.getId());
    }
}
