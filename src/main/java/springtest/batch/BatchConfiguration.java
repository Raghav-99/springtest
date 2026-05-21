package springtest.batch;

import org.springframework.batch.core.configuration.support.JdbcDefaultBatchConfiguration;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.infrastructure.item.ItemProcessor;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.batch.infrastructure.item.database.builder.JpaCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import jakarta.persistence.EntityManagerFactory;
import springtest.MessageEntity;

@Configuration
public class BatchConfiguration extends JdbcDefaultBatchConfiguration {
	/*
	 * private final JobInstanceDao jobInstanceDao; private final JobExecutionDao
	 * jobExecutionDao; private final StepExecutionDao stepExecutionDao; private
	 * final ExecutionContextDao executionContextDao;
	 */
	private final EntityManagerFactory entityManagerFactory;
	@Autowired
	public BatchConfiguration(
			EntityManagerFactory entityManagerFactory/*
														 * , JobInstanceDao jobInstanceDao, JobExecutionDao
														 * jobExecutionDao, StepExecutionDao stepExecutionDao,
														 * ExecutionContextDao executionContextDao
														 */) {
		this.entityManagerFactory = entityManagerFactory;
	}

	
	@Bean
	public Job toUpperJob(Step step, JobRepository jobRepository) {
		return new JobBuilder("toUpper", jobRepository).start(step).build();
	}
	
	@Bean
	public Step toUpperStep(PlatformTransactionManager transactionManager, JobRepository jobRepository) {
		return new StepBuilder(jobRepository).<MessageEntity, String>chunk(1)
				.reader(messageReader())
				.processor(messageProcessor())
				.writer(messageWriter()).transactionManager(transactionManager).build();
	}

	private ItemWriter<String> messageWriter() {
		return (chunk) -> {System.out.println(chunk.getItems().get(0));};
	}

	private ItemProcessor<MessageEntity, String> messageProcessor() {
		return item -> item.message.toUpperCase();
	}

	private ItemReader<MessageEntity> messageReader() {
		return new JpaCursorItemReaderBuilder<MessageEntity>().saveState(false).queryString("select msg from tblMessage msg").entityManagerFactory(entityManagerFactory).build();
	}
}
