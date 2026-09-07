package batchapp;

import org.springframework.batch.core.configuration.support.JdbcDefaultBatchConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import jakarta.persistence.EntityManagerFactory;
import sqsapp.SqsService;

@Configuration
public class BatchConfiguration extends JdbcDefaultBatchConfiguration {
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
}
