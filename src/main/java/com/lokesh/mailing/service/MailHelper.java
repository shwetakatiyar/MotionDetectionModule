/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lokesh.mailing.service;

import com.lokesh.encrypt.util.Config;
import com.lokesh.encrypt.util.CryptoException;
import com.lokesh.encrypt.util.CryptoHelper;
import com.lokesh.motion.usbcam.MotionMain;
import java.io.File;
import java.net.UnknownHostException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
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
 *
 * @author Lokesh
 */
public class MailHelper {

    public static boolean sendMail(String fileName) throws UnknownHostException{
        boolean success = false;
        // Recipient's email ID needs to be mentioned.
        String to = Config.RECIPIENT_EMAIL_ID.getValue();//change accordingly

        // Sender's email ID needs to be mentioned
        String from = Config.SENDER_EMAIL_ID.getValue(); //"loknavigator@gmail.com";//change accordingly
        final String username = Config.SENDER_EMAIL_ID.getValue(); //"loknavigator@gmail.com";//change accordingly
        String passwd ="";
        
        if (Config.DECRYPT_REQUIRED.getValueAsBoolean()) {
            try {
                passwd = CryptoHelper.decrypt(Config.SENDER_ENCRYPTED_PASSWORD.getValue());//change accordingly    
            } catch (CryptoException ex) {
                Logger.getLogger(MailHelper.class.getName()).log(Level.SEVERE, "Problem with sender password, not decrypted properly or wrong password ....", ex);
            }
        } else {
            passwd = Config.SENDER_ENCRYPTED_PASSWORD.getValue();
        }
        final String password= passwd;
        // Assuming you are sending email through relay.jangosmtp.net
//        String host = "smtp.gmail.com";

        Properties props = new Properties();
        props.put("mail.smtp.auth", Config.MAIL_SMTP_AUTH.getValue());
        props.put("mail.smtp.starttls.enable", Config.MAIL_SMTP_STARTTLS_ENABLE.getValue());
        props.put("mail.smtp.host", Config.MAIL_SMTP_HOST.getValue());
        props.put("mail.smtp.port", Config.MAIL_SMTP_PORT.getValue());

        // Get the Session object.
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            // Create a default MimeMessage object.
            Message message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress(from));

            // Set To: header field of the header.
            message.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(to));

            // Set Subject: header field
            message.setSubject("Testing Subject");

            // Create the message part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Now set the actual message
            messageBodyPart.setText("This is message body" + "/n" + "IP of you cam is : " + java.net.InetAddress.getLocalHost());

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

            // Part two is attachment
            messageBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(MotionMain.DIRECTORY_TO_WATCH + File.separatorChar + fileName);
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(fileName);
            multipart.addBodyPart(messageBodyPart);

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
            success = true;
            System.out.println("Sent message successfully....");

        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
        return success;
    }

}
