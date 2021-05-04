package AOITServer.Adapters;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

/**
 * ConcreteEmailClient class is used for sending simple emails by using Apache Email library.
 *
 * <p>Is part of an adapter pattern used to decouple specific email library from Server code</p>
 * @see AOITServer.Adapters.EmailClient
 */

public class ConcreteEmailClient implements EmailClient {

    private String gmail;
    private String password;
    private String username;

    /**
     *
     * @param gmail The email the host is sending the email from to client.
     * @param password The password of the email specified.
     */
    public ConcreteEmailClient(String gmail,String password){
        this.gmail = gmail;
        this.password = password;
    }

    /**
     * sendEmail method is used to send simple email with a subject and a message using Apache Email library.
     *
     * @param subject The subject of the contents of the message.
     * @param message The contents of the message being sent
     * @param receiver Email of the client receiving the email message.
     * @return Returns true if email was sent successfully , else returns false.
     */

    @Override
    public boolean sendEmail(String subject, String message, String receiver) {
        Email e = new SimpleEmail();
        e.setHostName("smtp.gmail.com");
        e.setSSLOnConnect(true);
        e.setSmtpPort(465);
        e.setAuthentication(gmail,password);

        try {

            e.setFrom(gmail);
            e.setSubject(subject);
            e.setMsg(message);
            e.addTo(receiver);
            e.send();

        }catch(EmailException er){
            System.out.println(er);
            return false;
        }

        return true;
    }
}
