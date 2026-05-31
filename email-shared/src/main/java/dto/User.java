package dto;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class User {
	private int id;
	private String email;    
    private List<Email> emails;
}
