package apiapp;

public interface IMessageService {
	void sendMessage(String message) throws Exception;
	MessageEntity retrieveMessage(int id) throws Exception;
}
