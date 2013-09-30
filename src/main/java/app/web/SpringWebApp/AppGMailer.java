package app.web.SpringWebApp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;
 
@Service("gmailService")
public class AppGMailer 
{
    @Autowired
    private MailSender gmailSender;
     
    /**
     * This method will send compose and send the message 
     * */
    public void sendMail(String to, String subject, String body) 
    {
        try 
        {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);
			gmailSender.send(message);
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}
    }

}