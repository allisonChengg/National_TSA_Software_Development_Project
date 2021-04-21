package AOITServer.Adapters;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

public class ConcreteEmailClient implements EmailClient {

    private String gmail;
    private String password;
    private String username;


    public ConcreteEmailClient(String gmail,String password){
        this.gmail = gmail;
        this.password = password;
    }

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
