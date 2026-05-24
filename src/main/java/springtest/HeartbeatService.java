package springtest;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

public abstract class HeartbeatService {
    protected static ScheduledExecutorService scheduledExecutor = Executors.newSingleThreadScheduledExecutor();
    public abstract ScheduledFuture<Void> checkHeartbeat(Callable<Void> runnable);
}
