package springtest;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity(name = "tblMessage")
@NoArgsConstructor
@AllArgsConstructor
public class MessageEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int id;
	@Column(nullable = false)
	public String message;
	@Column(nullable = false, columnDefinition = "varchar(12) default 'PENDING'", name = "status")
	public String messageStatus;
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("{\"id\":").append(id).append(",\"message\":").append("\"").append(message).append("\"}");
		return sb.toString();
	}
}
