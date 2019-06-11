import org.asyou.redis.util.IDGeneratorDelegate;

import java.util.Objects;

/**
 * Created by steven on 17/6/30.
 */
public class Delegate implements IDGeneratorDelegate {
    @Override
    public void doCallback(Object... args) {
        for(Object obj : args)
            System.out.println("DB DONE ! obj: "+obj.toString());
    }

    @Override
    public void doCallback() {
        System.out.println("doCallback is done!");
    }
}
