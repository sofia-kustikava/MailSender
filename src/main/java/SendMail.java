import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * SendMail class for SendMailer task
 */
public class SendMail {

    /**
     * This field contains logger for logging issues
     */
    private static final Logger LOGGER = LogManager.getLogger(SendMail.class);

    /**
     * This method contains logic for getting properties
     * @return Map of properties
     */
    private static Properties getMapOfProperties() {
        try (InputStream input = SendMail.class
                .getClassLoader()
                .getResourceAsStream(Constants.PROPERTIES_FILE)) {

            Properties prop = new Properties();
            prop.load(input);
            return prop;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    /**
     * This is main method for demonstration my task
     * @param args String[]
     */
    public static void main(String[] args) {
        Properties properties = getMapOfProperties();

        String receiver = properties.getProperty(Constants.RECEIVER);
        String sender = properties.getProperty(Constants.SENDER);
        String password = properties.getProperty(Constants.PASSWORD);

        Session session = Session.getInstance(properties, new javax.mail.Authenticator() {

            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(sender, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

            message.setFrom(new InternetAddress(sender));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(Constants.GREETINGS_USER);

            Multipart multipart = new MimeMultipart();
            MimeBodyPart textPart = new MimeBodyPart();

            textPart.setText(Constants.SAMPLE_MESSAGE);
            multipart.addBodyPart(textPart);


            message.setContent(multipart);
            Transport.send(message);
            LOGGER.info(Constants.SUCCESS_MESSAGE);
        } catch (MessagingException mex) {
            LOGGER.error(Constants.FAIL_MESSAGE);
            mex.printStackTrace();
        }

    }
}