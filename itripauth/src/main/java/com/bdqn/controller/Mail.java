package com.bdqn.controller;

import com.sun.mail.util.MailSSLSocketFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.util.Date;
import java.util.Properties;

public class Mail {
    public static void main(String[] args) throws GeneralSecurityException, MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        //设置邮件地址
        props.put("mail.smtp.host", "smtp.126.com");
        //开启认证
        props.put("mail.smtp.auth", "true");
        Session session = Session.getDefaultInstance(props, null);
        Transport transport = session.getTransport();
        //用户名
        String user = "wuzhang0310@126.com";
        //授权码
        String password = "WOKKJMHMHKLBTFCL";
        transport.connect(user, password);
        //创建邮件消息
        MimeMessage msg = new MimeMessage(session);
        msg.setSentDate(new Date());
        //邮件发送人
        InternetAddress fromAddress = new InternetAddress(user, "邮件服务");
        msg.setFrom(fromAddress);
        //邮件接收人
        String to = "wuzhang0310@126.com";
        InternetAddress[] toAddress = new InternetAddress[]{new InternetAddress(to)};
        msg.setRecipients(Message.RecipientType.TO, toAddress);
        //邮件主题
        msg.setSubject("爱旅行", "UTF-8");
        //邮件内容和格式
        msg.setContent("6666", "text/html;charset=UTF-8");
        msg.saveChanges();
        //发送
        transport.sendMessage(msg, msg.getAllRecipients());
    }
}
