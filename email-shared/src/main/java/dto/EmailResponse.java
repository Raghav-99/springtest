package dto;

public record EmailResponse(Email email, Exception exception) {
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Email: %s\r\nError: %s", email.toString(), exception.getMessage()));
        return sb.toString();
    }
}
