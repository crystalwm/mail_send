package emai.test;

import htmlToPDF.ConvertToPDF;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import mail.email.SimpleMailSender;
import mail.util.EmailConfigurationReader;

import oracle.pool.database.DBConnect;

import wingsoft.barcode.barcode.GenerateBarCode;

public class testhtmltopdf {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		DBConnect db=new DBConnect();
		Connection conn=db.getConnection();
		String paper_id="123456";
		String sql="select * from view_check_papers where paper_id='"+paper_id +"'";
		 Statement stmt = null;	
		 stmt = conn.createStatement();
		 ResultSet res=null;
		 if (stmt != null) {
			  res= stmt.executeQuery(sql);
			  
		//	  conn.close();
			}
		 
		/*
		 * 1:获得条形码
		 * 2：获得html
		 * 3：获得pdf
		 * 4：邮件发送
		 * */
			//获取Map对象
			Map<String,String> paper = new HashMap<String,String>(); 
			
			
//			  ResultSetMetaData rsmd = res.getMetaData();   
//			    int columnCount = rsmd.getColumnCount();   
//			    // 输出列名   
//			    for (int i=1; i<=columnCount; i++){   
//			        System.out.print(rsmd.getColumnName(i));   
//			        System.out.print("(" + rsmd.getColumnTypeName(i) + ")");   
//			        System.out.print(" | ");   
//			    }  
			
			
			
			while(res.next()){
  
				paper.put("paper_id", res.getString("paper_id"));
				paper.put("authors",  res.getString("authors"));
				paper.put("unitlist",  res.getString("unitlist"));
				paper.put("paper_name",  res.getString("paper_name"));
				paper.put("pnmae",  res.getString("pnmae"));
				paper.put("ab_code",  res.getString("ab_code"));
				paper.put("pub_date",  res.getString("pub_date"));
				paper.put("con_level",  res.getString("con_level"));
				paper.put("p_type", res.getString("p_type"));
				paper.put("wf_comment",  res.getString("wf_comment"));
				paper.put("paper_ord",  res.getString("paper_ord"));
				
			}
			conn.close();
			
			//获取条形码
	    	GenerateBarCode gbc=new GenerateBarCode();
			String dirpath = System.getProperty("user.dir");
			String savepath_barcode = dirpath + "/pdf/" ;
	    	gbc.generateQRCode(savepath_barcode+paper.get("paper_ord")+".png", paper.get("paper_id"));
	    	System.out.println("获取条形码生成成功！");
			
			//获取html
	    	ConvertToPDF ctp=new ConvertToPDF();
			String  url = dirpath + "/WebRoot/WEB-INF/mb.html";
			String savepath_html = dirpath + "/pdf/" +paper.get("paper_ord")+".html";
			
			ctp.ToHtmlFile(url, savepath_html,paper);
			System.out.println("获取html生成成功！");
			
			String savepath_from_html=  "pdf/" +paper.get("paper_ord")+".html";
			String savepath_pdf=  "pdf/" +paper.get("paper_ord")+".pdf";
			
			ctp.createPdf("D:\\wkhtmltopdf\\bin\\wkhtmltopdf.exe ", savepath_from_html, savepath_pdf);
			System.out.println("生成pdf成功！");
	
			//发送email
			String recipientsString ="1207356200@qq.com";
			String subject="教师信息管理平台（论文申报表格下载）";
			String content="教师信息管理平台（论文申报表格下载）";
			String isSingle="single";
			
			    EmailConfigurationReader ect = new EmailConfigurationReader();

//			    String emailConfFile = WebInitServlet.DeploymentPath + 
//			      "WEB-INF" + 
//			      File.separator + "emailConfigure.xml";
			  
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
			    File affix = new File("pdf/"+paper.get("paper_ord")+".pdf");
			    
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


