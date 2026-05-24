package springtest.sqs;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import software.amazon.awssdk.services.sqs.model.*;

import java.util.concurrent.Future;

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
	
	public ReceiveMessageResponse consumeMessage(String queueName) {
		Assert.hasLength(queueName, "Queue name cannot be empty");
		String queueUrl = createQueueUrl(queueName);
		
		ReceiveMessageRequest.Builder builder = ReceiveMessageRequest.builder();
		builder.queueUrl(queueUrl);
        builder.messageSystemAttributeNames(MessageSystemAttributeName.APPROXIMATE_FIRST_RECEIVE_TIMESTAMP);
		ReceiveMessageRequest request = builder.build();
		return this.sqsClient.getSqsClient().receiveMessage(request);
	}

    public DeleteMessageResponse deleteMessage(String queueName, String receipt) {
        Assert.hasLength(queueName, "Queue name cannot be empty");
        String queueUrl = createQueueUrl(queueName);

        DeleteMessageRequest.Builder builder = DeleteMessageRequest.builder();
        builder.queueUrl(queueUrl);
        builder.receiptHandle(receipt);
        DeleteMessageRequest request = builder.build();
        return this.sqsClient.getSqsClient().deleteMessage(request);
    }

    public Void checkExtendVisiblity(Message message, Future<?> work) {
        if(!work.isDone()) {
            this.sqsClient.getSqsClient().changeMessageVisibility((builder) -> builder.receiptHandle(message.receiptHandle()).visibilityTimeout(30).build());
        }
        return null;
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
