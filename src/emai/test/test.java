package emai.test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import mail.email.SimpleMailSender;
import mail.util.EmailConfigurationReader;

public class test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String recipientsString ="1207356200@qq.com";
		String subject="测试";
		String content="rrrr";
		String isSingle="single";
		
		    EmailConfigurationReader ect = new EmailConfigurationReader();

//		    String emailConfFile = WebInitServlet.DeploymentPath + 
//		      "WEB-INF" + 
//		      File.separator + "emailConfigure.xml";
		  
		    String emailConfFile = null;
			try {
				emailConfFile = (new File("")).getCanonicalPath()+
						"/src" +
						"/emailConfigure.xml";
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
           
		    ect.readConfiguration(emailConfFile);
		    
		    //连接附件
		    File affix = new File("D:\\test.sql");
		    
		    SimpleMailSender smSender = 
		    		new SimpleMailSender(ect.getHostname(), ect.getUsername(), ect.getPassword(),affix);

		    long sqlStartTime = new Date().getTime();
		    try
		    {
		      if ("single".equals(isSingle)) {
		        smSender.sendSimpleAsSingle(recipientsString, subject, content);
		      }
		      else {
		        smSender.sendSimpleAsMultiple(recipientsString, subject, content);
		      }
		  
		      long execTime = new Date().getTime() - sqlStartTime;
		      
		    }
		    catch (Exception e) {
		      e.printStackTrace();
		    }


	}

}
