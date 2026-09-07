package batchapp;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.launch.JobOperator;
import sqsapp.SqsService;

@RequiredArgsConstructor
public class MessageJobController {
	private final JobOperator jobOperator;
	private final Job job;
    private final SqsService sqsService;

}
