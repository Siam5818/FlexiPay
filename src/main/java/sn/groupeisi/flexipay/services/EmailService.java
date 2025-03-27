package sn.groupeisi.flexipay.services;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import sn.groupeisi.flexipay.utils.ConfigUtil;
import java.util.Properties;

public class EmailService {
    public void sendEmail(String recipient, String subject, String body) {
        Properties properties = ConfigUtil.loadConfig();

        String username = properties.getProperty("mail.smtp.username");
        String password = properties.getProperty("mail.smtp.password");

        // Parameter de configuration SMTP

        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", properties.getProperty("mail.smtp.host"));
        properties.put("mail.smtp.port", properties.getProperty("mail.smtp.port"));

        // Création de la session de mail avec authentification
        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // Retourner l'objet PasswordAuthentication correctement
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            // Création du message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
            message.setSubject(subject);
            message.setText(body);

            // Envoi du message
            Transport.send(message);
            System.out.println("Email envoyé à " + recipient);
        } catch (MessagingException e) {
            System.err.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
}
