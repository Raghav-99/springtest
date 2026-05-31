package dto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Attachment {
	private int id;
	private byte[] blob;
}