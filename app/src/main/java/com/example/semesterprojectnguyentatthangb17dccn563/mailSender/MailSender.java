package com.example.semesterprojectnguyentatthangb17dccn563.mailSender;

import java.security.Security;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class MailSender extends Authenticator {
    private String host = "smtp.gmail.com";
    private String user = "boomspeed20@gmail.com";
    private String password = "xthangbomx";
    private Session session;

    static {
        Security.addProvider(new JSSEProvider());
    }

    public MailSender() {
        Properties propSend = new Properties();
        propSend.setProperty("mail.transport.protocol", "smtp");
        propSend.setProperty("mail.host", host);
        propSend.put("mail.smtp.auth", "true");
        propSend.put("mail.smtp.port", "465");
        propSend.put("mail.smtp.socketFactory.port", "465");
        propSend.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        propSend.put("mail.smtp.socketFactory.fallback", "false");
        propSend.setProperty("mail.smtp.quitwait", "false");

        session = Session.getDefaultInstance(propSend,this);
    }

    protected PasswordAuthentication getPasswordAuthentication(){
        return new PasswordAuthentication(user, password);
    }

    public synchronized void sendMail(String subject, Multipart attachment, String sender, String recipient) throws Exception {
        MimeMessage message = new MimeMessage(session);
        message.setSender(new InternetAddress(sender));
        message.setSubject(subject);
        if(attachment != null){
            message.setContent(attachment);
        }
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));

        Transport.send(message);
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
