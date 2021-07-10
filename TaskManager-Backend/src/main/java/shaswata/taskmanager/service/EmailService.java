package shaswata.taskmanager.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class EmailService {

    private final String systemEmail = "${spring.mail.username}";

    @Autowired
    private JavaMailSender javaMailSender;


    @Async
    public void accountCreationEmail(String recipientEmail, String name) {

        CompletableFuture<SimpleMailMessage> email = new CompletableFuture<>();

        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setFrom(systemEmail);
        msg.setTo(recipientEmail);
        msg.setSubject("Account Created - Task Manager");
        msg.setText("Hello " + name + ", \n\nYou have successfully created an account for Task Manager.\n" +
                "You are now ready to log in to Task Manager!");

        email.complete(msg);
        try {
            Thread.sleep(5000);     //send mail after 5 seconds
            javaMailSender.send(msg);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

}
