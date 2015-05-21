package mail.util;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class EmailConfigurationReader
{
  String username = "";
  String password = "";
  String hostname = "";

  public String getUsername() {
    return this.username; }

  public String getPassword() {
    return this.password; }

  public String getHostname() {
    return this.hostname; }

  public void readConfiguration(String path) {
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    try
    {
      DocumentBuilder db = dbf.newDocumentBuilder();
      Document doc = db.parse("file:///" + path);

      NodeList usernameList = doc.getElementsByTagName("username");
      NodeList passwordList = doc.getElementsByTagName("password");
      NodeList hostnamelList = doc.getElementsByTagName("smtpHostname");

      this.username = usernameList.item(0).getFirstChild().getNodeValue();
      this.password = passwordList.item(0).getFirstChild().getNodeValue();
      this.hostname = hostnamelList.item(0).getFirstChild().getNodeValue();
    }
    catch (Exception e)
    {
      e.printStackTrace(); }
  }

  public static void main(String[] args) {
    EmailConfigurationReader ecr = new EmailConfigurationReader();
    ecr.readConfiguration("");
  }
}