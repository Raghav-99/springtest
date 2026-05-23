package springtest.sqs;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.services.sqs.SqsClient;

@RequiredArgsConstructor
@Getter
public class ExtraSqsClient {
	private final SqsClient sqsClient;
	private final String url;
}
