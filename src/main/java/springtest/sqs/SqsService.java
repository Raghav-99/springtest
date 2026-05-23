package springtest.sqs;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
public class SqsService implements InitializingBean {
	private final ExtraSqsClient sqsClient;
	@Autowired
	public SqsService(ExtraSqsClient sqsClient) {
		this.sqsClient = sqsClient;
	}
	
	public void sendMessage(String message, String queueName) {
		Assert.hasLength(queueName, "Queue name cannot be empty");
		
		String queueUrl = createQueueUrl(queueName);
		// this returns the nested builder instance of sendmessagerequest obj
		// hence the obj of sendmessagerequest is created
		SendMessageRequest.Builder builder = SendMessageRequest.builder();
		builder.queueUrl(queueName);
		builder.messageBody(message.toString());
		SendMessageRequest request = builder.build();
		this.sqsClient.getSqsClient().sendMessage(request);
	}
	
	public void consumeMessage(String queueName) {
		Assert.hasLength(queueName, "Queue name cannot be empty");
		String queueUrl = createQueueUrl(queueName);
		
		ReceiveMessageRequest.Builder builder = ReceiveMessageRequest.builder();
		builder.queueUrl(queueUrl);
		ReceiveMessageRequest request = builder.build();
		this.sqsClient.getSqsClient().receiveMessage(request);
	}
	
	private String createQueueUrl(String queueName) {
		return this.sqsClient.getUrl().concat(queueName);
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.notNull(sqsClient, "The sqsClient cannot be null!");
		Assert.hasLength(this.sqsClient.getUrl(), "The sqs endpoint URL cannot be empty!");
	}
	
}
