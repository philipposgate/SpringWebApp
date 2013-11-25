package app.common.google;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import app.common.AppConfigDAO;

@Service("gmailService")
public class GoogleEmailerService implements InitializingBean
{
    @Autowired
    private JavaMailSenderImpl gmailSender;
     
    @Autowired
	private AppConfigDAO appConfigDAO;
    
    /**
     * This method will send compose and send the message 
     * */
    public boolean sendMail(String to, String subject, String body) 
    {
    	boolean sent = false;
        try 
        {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setTo(to);
			message.setSubject(subject);
			message.setText(body);
			
			gmailSender.send(message);
			
			sent = true;
		} 
        catch (Exception e) 
        {
			e.printStackTrace();
		}

        return sent;
    }

	@Override
	public void afterPropertiesSet() throws Exception 
	{
		resetUsernameAndPassword();
	}

	public void resetUsernameAndPassword() 
	{
		gmailSender.setUsername(appConfigDAO.getValue("gmailUsername"));
		gmailSender.setPassword(appConfigDAO.getValue("gmailPassword"));
	}
}