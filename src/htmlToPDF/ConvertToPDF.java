package htmlToPDF;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Map;

public class ConvertToPDF {
	public void createPdf(String toolFileName,String htmlFileName,String pdfFileName) throws Exception{
		//"D:\\wkhtmltopdf\\bin\\wkhtmltopdf.exe "
		String command =toolFileName +htmlFileName + " " + pdfFileName;  
        System.out.println(command);  
        File pdfFile = new  File(pdfFileName); 
        if(!pdfFile.exists()){
        	pdfFile.createNewFile();
        }  
        Runtime.getRuntime().exec(command);  
	}
	
	
	//利用模板转换成html
	
	 public static boolean ToHtmlFile(String filePath,String HtmlFile,Map<String,String> paper) {
         String str = "";
         long beginDate = (new Date()).getTime();
         try {
                 String tempStr = "";
                FileInputStream is = new FileInputStream(filePath);//读取模块文件
                 BufferedReader br = new BufferedReader(new InputStreamReader(is));
                 while ((tempStr = br.readLine()) != null)
                 str = str + tempStr ;
                 is.close();
         } catch (IOException e) {
                 e.printStackTrace();
                 return false;
         }
         try {
            
     //替换掉相应的地方
     for(Map.Entry<String, String> entry:paper.entrySet()){   
    	 System.out.println(entry.getKey()+"--->"+entry.getValue()); 
    	 str=str.replaceAll('#'+entry.getKey()+'#', entry.getValue()==null?"":entry.getValue());
         
    }   
     
                 File f = new File(HtmlFile);
                 BufferedWriter o = new BufferedWriter(new FileWriter(f));
                 o.write(str);
                 o.close();
                 System.out.println("共用时：" + ((new Date()).getTime() - beginDate) + "ms");
         } catch (IOException e) {
                 e.printStackTrace();
                 return false;
         }
         return true;
 }

}
