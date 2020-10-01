package rc.mail.feedback;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;


import javax.validation.ValidationException;

@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    private EmailCfg emailCfg;

    public FeedbackController(EmailCfg emailCfg) {
        this.emailCfg = emailCfg;
    }

    @PostMapping
    public void sendFeedback(@RequestBody Feedback feedback,
                             BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            throw new ValidationException("Feedback is not valid");
        }

        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
        mailSender.setPort(this.emailCfg.getPort());
        mailSender.setUsername(this.emailCfg.getUsername());
        mailSender.setPassword(this.emailCfg.getPassword());
        mailSender.setHost(this.emailCfg.getHost());
        

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setFrom(feedback.getEmail());
        mailMessage.setTo("rc@gmail.com");
        mailMessage.setSubject("my feedback from " + feedback.getName());
        mailMessage.setText(feedback.getFeedback());

        // Send mail
        mailSender.send(mailMessage);
    }
}
