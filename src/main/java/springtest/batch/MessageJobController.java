package springtest.batch;

import java.util.Set;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.JobExecutionException;
import org.springframework.batch.core.job.parameters.JobParameter;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import springtest.sqs.SqsService;

@RestController
@RequestMapping("/job")
public class MessageJobController {
	private final JobOperator jobOperator;
	private final Job job;
	
	@Autowired
	public MessageJobController(JobOperator jobOperator, @Autowired Job job) {
		this.jobOperator = jobOperator;
		this.job = job;
	}
	
	@GetMapping("/start/{id}")
	public ResponseEntity<String> start(@PathVariable(value = "id", required = true) String id) throws JobExecutionException {
		if(id.isBlank() || id == null) throw new JobExecutionException("Id cannot be blank!");
		JobExecution jobExecution = jobOperator.start(job, new JobParameters(Set.of(new JobParameter<>("Id", Integer.parseInt(id), Integer.class))));
		return ResponseEntity.ok(String.format("Start id: %d\r\nStatus: %s\r\n", jobExecution.getId(), jobExecution.getStatus()));
	}
	
	@ExceptionHandler(value = {JobExecutionException.class})
	public ResponseEntity<String> handleJobError(JobExecutionException ex) {
		return ResponseEntity.internalServerError().body(ex.getMessage());
	}
}
