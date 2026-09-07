package batchapp;

import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jakarta.persistence.EntityManagerFactory;
import sqsapp.SqsService;

@Configuration 
public class InvoiceJob extends BatchConfiguration implements MessageJobListener {
    public InvoiceJob(EntityManagerFactory entityManagerFactory, SqsService sqsService) {
        super(entityManagerFactory, sqsService);
    }

    @Override
    public String getQueueName() {
        return "monthly-invoice-queue";
    }

    @Bean("invoiceJob")
    Job invoiceJob(JobRepository jobRepository) {
        return new JobBuilder("monthlyinvoicejob", jobRepository).start(accumulateInvoiceStep()).flow().build();
    }
}
