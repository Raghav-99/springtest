package dto;

import java.util.List;

public record EmailRequest(String sender, List<String> recepients, String subject, String body, String attachment) {}
