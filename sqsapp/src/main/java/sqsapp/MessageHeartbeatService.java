package sqsapp;

import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@Component
public class MessageHeartbeatService extends HeartbeatService {
    @Override
    public ScheduledFuture<Void> checkHeartbeat(Callable<Void> callable) {
        return scheduledExecutor.schedule(callable, 10, TimeUnit.SECONDS);
    }
}
