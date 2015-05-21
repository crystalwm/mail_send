package mail.util;

import java.util.ArrayList;
import java.util.List;

public class RecipientsReader
{
  public static List<String> read(String recipientsString)
  {
    ArrayList recipients = new ArrayList();
    if (recipientsString.indexOf(";") > 0) {
      String[] recipientsStrings = recipientsString.split(";");
      for (int i = 0; i < recipientsStrings.length; ++i)
        recipients.add(recipientsStrings[i].trim());
    }
    else {
      recipients.add(recipientsString);
    }
    return recipients;
  }

  public static String getRecipientName(String recipient) {
    return recipient.split("@")[0];
  }
}