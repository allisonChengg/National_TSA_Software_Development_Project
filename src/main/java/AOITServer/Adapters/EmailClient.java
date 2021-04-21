package AOITServer.Adapters;

public interface EmailClient {
    boolean sendEmail(String subject, String message,String receiver);
}
