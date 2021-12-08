package dss;

import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class Mail {
    public static Session session;

    public static void inicializarMail() {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        session = Session.getInstance(prop, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("dss.reparacoes@gmail.com", "dss_2021");
            }
        });
    }

    public static void enviaMail(String destinatario, String assunto, String mensagem) {
        Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress("dss.reparacoes@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO, InternetAddress.parse(destinatario));

        message.setSubject(assunto);

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(mensagem, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);

        Transport.send(message);
        }
        catch (MessagingException e) {
            System.out.println("Erro no envio do email.");
            e.printStackTrace();
        }
    }
}
