package batchapp;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.parameters.InvalidJobParametersException;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.launch.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageResponse;
import sqsapp.SqsService;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;

public interface MessageJobListener {
    default JobExecution fireJob(JobOperator jobOperator, Job job, SqsService sqsService) throws JobInstanceAlreadyCompleteException, InvalidJobParametersException, JobExecutionAlreadyRunningException, JobRestartException {
        ReceiveMessageResponse message = sqsService.consumeMessage(getQueueName());
        JobParameters jobParameters = null;
        if(message.hasMessages()) {
            List<Message> messageList = message.messages();
            jobParameters = new JobParametersBuilder()
                    .addString("messageId", messageList.get(0).messageId())
                    .addString("messageBody", messageList.get(0).body())
                    .toJobParameters();
        }
        return jobOperator.start(job, jobParameters);
    }

    String getQueueName();
}
