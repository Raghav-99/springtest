package springtest.batch;

import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.annotation.AfterJob;
import org.springframework.batch.core.configuration.support.JdbcDefaultBatchConfiguration;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.JobExecution;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.listener.JobExecutionListener;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.batch.infrastructure.item.database.builder.JpaItemWriterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import springtest.MessageEntity;
import springtest.MessageStatus;
import springtest.sqs.SqsService;

@Configuration
public class BatchConfiguration extends JdbcDefaultBatchConfiguration implements JobExecutionListener {
	/*
	 * private final JobInstanceDao jobInstanceDao; private final JobExecutionDao
	 * jobExecutionDao; private final StepExecutionDao stepExecutionDao; private
	 * final ExecutionContextDao executionContextDao;
	 */
	private final EntityManagerFactory entityManagerFactory;
	private final SqsService sqsService;
	@Autowired
	public BatchConfiguration(
			EntityManagerFactory entityManagerFactory, SqsService sqsService/*
														 * , JobInstanceDao jobInstanceDao, JobExecutionDao
														 * jobExecutionDao, StepExecutionDao stepExecutionDao,
														 * ExecutionContextDao executionContextDao
														 */) {
		this.entityManagerFactory = entityManagerFactory;
		this.sqsService = sqsService;
	}

	
	@Bean
	public Job toUpperJob(Step step, JobRepository jobRepository) {
		return new JobBuilder("toUpper", jobRepository).listener(this.new MessageJobListener()) .start(step).build();
	}
	
	@Bean
	public Step toUpperStep(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
		return new StepBuilder(jobRepository).<MessageEntity, MessageEntity>chunk(1)
				.reader(messageReader())
				.processor(messageProcessor())
				.writer(messageWriter()).transactionManager(transactionManager).build();
	}

	private ItemWriter<MessageEntity> messageWriter() {
		JpaItemWriterBuilder<MessageEntity> builder = new JpaItemWriterBuilder<>();
		return builder.entityManagerFactory(entityManagerFactory).build();
	}

	private ItemProcessor<MessageEntity, MessageEntity> messageProcessor() {
		ItemProcessor<MessageEntity, MessageEntity> item = i -> {
			i.messageStatus = MessageStatus.PROCESSED.name();
			i.message = i.message.toUpperCase();
			return i;
		};
		return item;
	}

	private ItemReader<MessageEntity> messageReader() {
		return new JpaCursorItemReaderBuilder<MessageEntity>().name("jpa.itemreader").queryString("select msg from tblMessage msg where msg.messageStatus='PENDING' OR msg.messageStatus='ERROR'").entityManagerFactory(entityManagerFactory).build();
	}
	
	class MessageJobListener implements JobExecutionListener {
		@Override
		public void afterJob(JobExecution jobExecution) {
			if(jobExecution.getExitStatus().equals(ExitStatus.COMPLETED)) {
				System.out.println("Job is done. Sending job execution to SQS...");
				sqsService.sendMessage(jobExecution.toString(), "ExperimentQ");
				System.out.println("Sent message: "+ jobExecution.toString());
			}
		}
	}
}
