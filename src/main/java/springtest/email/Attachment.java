package springtest.email;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class Attachment {
	private final String id;
	private final String emailId;
	private final byte[] blob;
}
