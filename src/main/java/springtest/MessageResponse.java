package springtest;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageResponse {
	private MessageEntity entity;
	private String error;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"entity\":").append(entity.toString()).append(",").append("\"error\":").append(error).append("}");
		return sb.toString();
	}
}
