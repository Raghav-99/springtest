package springtest.sqs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import software.amazon.awssdk.services.sqs.SqsClient;

@Configuration
public class SqsConfiguration {
	@Bean
	public ExtraSqsClient extraSqsClient(@Value("${aws.sqs.url}") String url) {
		return new ExtraSqsClient(SqsClient.create(), url);
	}
	
}
