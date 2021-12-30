package dss.business;

import net.sargue.mailgun.Configuration;
import net.sargue.mailgun.Mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Email {
    Configuration config;

    public Email() throws FileNotFoundException {
        Properties prop = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("config/config.properties");

        try {
            prop.load(inputStream);
        } catch (Exception e) {
            throw new FileNotFoundException("Ficheiro de configuração não existe");
        }

        config = new Configuration()
                .domain(prop.getProperty("mailgun.domain"))
                .apiKey(prop.getProperty("mailgun.key"))
                .from(prop.getProperty("mailgun.from"), prop.getProperty("mailgun.email"))
                .apiUrl(prop.getProperty("mailgun.apiUrl"));

    }

    public void enviaMail(String destinatario, String assunto, String mensagem) {
        Mail.using(config)
            .to(destinatario)
            .subject(assunto)
            .text(mensagem)
            .build().send();
    }
}
