package springtest.sqs;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;

import java.util.concurrent.*;

@Component
final class PollingService {
    private final SqsService sqsService;
    private final MessageHeartbeatService messageHeartbeatService;
    @Autowired
    private PollingService(SqsService sqsService, MessageHeartbeatService messageHeartbeatService) {
        this.sqsService = sqsService;
        this.messageHeartbeatService = messageHeartbeatService;
    }
    @EventListener(ApplicationReadyEvent.class)
    void poll() {
        Thread thread = new Thread(this.new PollRunner());
        thread.start();
        System.out.println("Spawned thread "+ Thread.currentThread());
    }

    private class PollRunner implements Runnable {
        @Override
        public void run() {
            try {
                while (!Thread.currentThread().isInterrupted()) {
                    ReceiveMessageResponse response = sqsService.consumeMessage("ExperimentQ");;
                    if(response.hasMessages()) {
                        Message message = response.messages().get(0);
                        CompletableFuture<Void> task = CompletableFuture.runAsync(() -> doWork(message));
                        ScheduledFuture<Void> heartbeat = messageHeartbeatService.checkHeartbeat(() -> sqsService.checkExtendVisiblity(message, task));
                        task.whenComplete((status, err) -> {
                            if(err == null) {
                                heartbeat.cancel(false);
                                sqsService.deleteMessage("ExperimentQ", message.receiptHandle());
                            }
                        });
                    }
                    Thread.currentThread().sleep(60000);
                }
            }
            catch(InterruptedException e){
                System.out.println("Thread " + Thread.currentThread().getId() + " interrupted!");
            } catch (Exception e) { // rethrow business work related exception
                throw new RuntimeException(e);
            }
        }

        // heavy i/o work
        private void doWork(Message message) {
            System.out.println(message.body());
        }
    }
}
