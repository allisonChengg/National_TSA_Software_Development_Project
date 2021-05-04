package AOITServer.Adapters;

/**
 * Implementing EmailClient interface allows for sub class to specify how to send the email.
 *
 * <p>Is part of an adapter pattern used to decouple specific email library from Server code</p>
 */
public interface EmailClient {
    /**
     * sendEmail method is used to send simple email with a subject and a message.
     *
     * @param subject The subject of the contents of the message.
     * @param message The contents of the message being sent
     * @param receiver Email of the client receiving the email message.
     * @return Returns true if email was sent successfully , else returns false.
     */
    boolean sendEmail(String subject, String message,String receiver);
}
