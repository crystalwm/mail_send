package mail.email;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import mail.util.RecipientsReader;

public class SimpleMailSender
{
  private final transient Properties props = System.getProperties();
  private transient EmailAuthenticator authenticator;
  private transient Session session;
  private File attachment=null;
  

  public SimpleMailSender(String smtpHostName, String username, String password,File attachment)
  {
    this.attachment=attachment;
    init(username, password, smtpHostName);
  }
  
  public SimpleMailSender(String smtpHostName, String username, String password)
  {
    init(username, password, smtpHostName);
  }


  public SimpleMailSender(String username, String password)
  {
    String smtpHostName = "mail." + username.split("@")[1];
    init(username, password, smtpHostName);
  }

  private void init(String username, String password, String smtpHostName)
  {
    this.props.put("mail.smtp.auth", "true");
    this.props.put("mail.smtp.host", smtpHostName);
    this.props.put("username", username);
    this.props.put("password", password);

    this.authenticator = new EmailAuthenticator(username, password);

    this.session = Session.getInstance(this.props, this.authenticator);
  }

  public void sendSimpleAsSingle(String recipientsString, String subject, String content)
    throws ErrorRecipientException
  {
    List recipients = RecipientsReader.read(recipientsString);

    List errorRecipientsList = new LinkedList();
    for (Iterator iterator = recipients.iterator(); iterator
      .hasNext(); )
    {
      String recipient = (String)iterator.next();

      String recipientName = RecipientsReader.getRecipientName(recipient);

      MimeMessage message = new MimeMessage(this.session);
      try
      {
        message.setFrom(new InternetAddress(this.authenticator.getUsername()));

        message.setRecipient(MimeMessage.RecipientType.TO, 
          new InternetAddress(recipient));

        message.setSubject(subject);

        
     // 向multipart对象中添加邮件的各个部分内容，包括文本内容和附件
        Multipart multipart = new MimeMultipart();
        
     
     // 添加邮件正文
        BodyPart contentPart = new MimeBodyPart();
        contentPart.setContent(
                content.replaceAll("@recipientName", recipientName), 
                "text/html;charset=utf-8");
        multipart.addBodyPart(contentPart);
        
        // 添加附件的内容
        if (attachment != null) {
            BodyPart attachmentBodyPart = new MimeBodyPart();
            DataSource source = new FileDataSource(attachment);
            attachmentBodyPart.setDataHandler(new DataHandler(source));
            
            // 网上流传的解决文件名乱码的方法，其实用MimeUtility.encodeWord就可以很方便的搞定
            // 这里很重要，通过下面的Base64编码的转换可以保证你的中文附件标题名在发送时不会变成乱码
            //sun.misc.BASE64Encoder enc = new sun.misc.BASE64Encoder();
            //messageBodyPart.setFileName("=?GBK?B?" + enc.encode(attachment.getName().getBytes()) + "?=");
            
            //MimeUtility.encodeWord可以避免文件名乱码
            try {
				attachmentBodyPart.setFileName(MimeUtility.encodeWord(attachment.getName()));
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            multipart.addBodyPart(attachmentBodyPart);
        }
        
        // 将multipart对象放到message中
        message.setContent(multipart);
        // 保存邮件
        message.saveChanges();


        Transport.send(message);
      }
      catch (AddressException e) {
        errorRecipientsList.add(recipient);
      }
      catch (MessagingException e) {
    	  
        errorRecipientsList.add(recipient);
      }
    }
    if (errorRecipientsList.size() <= 0)
      return;
    throw new ErrorRecipientException(errorRecipientsList);
  }

  public void sendSimpleAsMultiple(String recipientsString, String subject, String content)
    throws ErrorRecipientException
  {
    List recipients = RecipientsReader.read(recipientsString);

    MimeMessage message = new MimeMessage(this.session);
    try
    {
      message.setFrom(new InternetAddress(this.authenticator.getUsername()));

      int num = recipients.size();
      InternetAddress[] addresses = new InternetAddress[num];
      for (int i = 0; i < num; ++i) {
        addresses[i] = new InternetAddress((String)recipients.get(i));
      }
      message.setRecipients(MimeMessage.RecipientType.TO, addresses);

      message.setSubject(subject);

      MimeMultipart email = new MimeMultipart();

      MimeBodyPart contentPart = new MimeBodyPart();

      contentPart.setContent(content, "text/html;charset=UTF-8");
      email.addBodyPart(contentPart);

      Transport.send(message);
    }
    catch (MessagingException e) {
      throw new ErrorRecipientException(recipients);
    }
  }
}