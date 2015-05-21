package mail.util;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class AttachmentGetter
{
  public static List<File> getFiles()
  {
    List files = new LinkedList();
    File testFile = new File("test.txt");
    files.add(testFile);
    return files;
  }
}