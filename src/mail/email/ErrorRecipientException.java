package mail.email;

import java.util.Iterator;
import java.util.List;

public class ErrorRecipientException extends Exception
{
  List<String> errorRecipientsList;

  public ErrorRecipientException(List<String> errorRecipientsList)
  {
    this.errorRecipientsList = errorRecipientsList;
  }

  public List<String> getErrorRecipients() {
    return this.errorRecipientsList;
  }

  public String getMessage()
  {
    String message = "";
    for (Iterator iterator = this.errorRecipientsList.iterator(); iterator.hasNext(); ) {
      String errorRecipient = (String)iterator.next();
      message = message + errorRecipient;
      message = message + ";";
    }
    return message;
  }
}
