package com.nurflugel.picturebrowserservlet.htmlstuff;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.UtilMethods;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import com.nurflugel.picturebrowserservlet.gui.MainFrame;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Category;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import static org.apache.commons.io.FileUtils.copyFile;

/**  */
public class HtmlWriter
{
  private Category        logger         = LogFactory.getInstance(HtmlWriter.class);
  private Dir             dirpage;
  private File            currentDir;
  private MainFrame       mainFrame;
  private int             numColumns;
  private List<MediaFile> pics           = new ArrayList<MediaFile>();
  private int             numRowsPerPage;
  private int             numPages;
  private ClassLoader     classloader;
  private Skin            skin;
  private NurFont         titleFont;
  private NurFont         captionFont;
  private NurFont         dirFont;

  /** Creates a new HtmlWriter object. */
  public HtmlWriter(MainFrame mainFrame)
  {
    this.mainFrame = mainFrame;
    init();
    classloader = getClass().getClassLoader();
  }

  /**  */
  private void init()
  {
    dirpage    = mainFrame.getDirpage();
    numColumns = mainFrame.getNumColumns();
    currentDir = mainFrame.getCurrentDir();
    pics       = dirpage.getPics();

    int numRows = pics.size() / mainFrame.getNumColumns();

    numRowsPerPage = UtilMethods.getNumRowsFromString(mainFrame.getNumRowsPerPageFromDropdown());
    numPages       = numRows / numRowsPerPage;
  }
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  public void saveHtml(Skin skin) throws IOException
  {
    String[] skinFilesToCopy   = skin.getFilesToCopy();
    String[] systemFilesToCopy = UtilMethods.FILES_TO_COPY;

    init();

    String systemFileName;

    // int            size              = systemFilesToCopy.length + skinFilesToCopy.length;
    // String[]       filesToCopy       = new String[size+20];
    for (String aSystemFilesToCopy : systemFilesToCopy)
    {
      systemFileName = "images/" + aSystemFilesToCopy;
      saveSystemImageFile(new File(systemFileName));
    }

    for (String aSkinFilesToCopy : skinFilesToCopy)
    {
      systemFileName = "images/" + aSkinFilesToCopy;

      try
      {
        saveSystemImageFile(new File(systemFileName));
      }
      catch (IOException e)
      {
        logger.error("Something bad happened", e);
      }
    }

    for (int page = 0; page <= numPages; page++)
    {
      saveHtmlPage(page, skin);
    }
  }

  /**  */
  private void saveSystemImageFile(File file) throws IOException
  {
    if (file != null)
    {
      if (file.exists())
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("HtmlWriter.saveSystemImageFile no WS");
        }

        saveMiscImageFiles(currentDir, file);
      }
      else
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("HtmlWriter.saveSystemImageFile WS version");
        }

        String fileName = file.getName();

        if (logger.isDebugEnabled())
        {
          logger.debug("fileName = " + fileName);
        }

        URL resource = classloader.getResource(fileName);  // We need to open the file like this, as it's in the WebStart archive

        if (logger.isDebugEnabled())
        {
          logger.debug("resource = " + resource);
        }

        InputStream inputStream = resource.openStream();

        if (logger.isDebugEnabled())
        {
          logger.debug("inputStream = " + inputStream);
        }

        saveMiscImageFiles(currentDir, fileName, inputStream);
      }
    }
    else
    {
      logger.error("saveSystemImageFile Error - image file isn't real!");
    }
  }

  /** Save the background image to the directory from the archived location. */
  private void saveMiscImageFiles(File currentDir, File sourceFile)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("sourceFile.getAbsolutePath() = " + sourceFile.getAbsolutePath());
    }

    File destFile = new File(currentDir, sourceFile.getName());

    try
    {
      copyFile(sourceFile, destFile);
    }
    catch (IOException e)
    {
      logger.error("Unable to copy file, but continuing anyway... " + sourceFile.getName(), e);
    }
  }

  /** Save the background image to the directory from the archived location. */
  private void saveMiscImageFiles(File currentDir, String fileName, InputStream is)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("HtmlWriter.saveMiscImageFiles");
    }

    File destFile = new File(currentDir, fileName);

    try
    {
      UtilMethods.copyFile(is, destFile);
    }
    catch (IOException e)
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("Error getting image", e);
      }
    }
  }

  /** todo replace with writelines. */
  private void saveHtmlPage(int page, Skin skin)
  {
    this.skin = skin;

    File file = (page == 0) ? new File(currentDir, "index.html")
                            : new File(currentDir, "index" + page + ".html");

    try
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("Preparing to write to file " + file.getAbsolutePath());
      }

      // FileOutputStream fileOutputStream     = new FileOutputStream(file, false  /* append */);
      String  backgroundFileName = skin.getBackgroundFileName();
      NurFont mainFont           = skin.getMainFont();

      titleFont   = skin.getTitleFont();
      captionFont = skin.getCaptionFont();
      dirFont     = skin.getDirFont();

      List<String> lines = new ArrayList<String>();

      lines.add("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">");
      lines.add("<HTML>");
      lines.add("<HEAD>");
      lines.add("<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=iso-8859-1\">");
      lines.add("<META NAME=\"Generator\" CONTENT=\"BullaSoft PictureBrowserServlet\">");
      lines.add("<TITLE>index</TITLE>");
      lines.add("</HEAD>");
      lines.add("<BODY BGCOLOR=\"#FFFFFF\" BACKGROUND=\"./" + backgroundFileName
                  + "\" TEXT=\"#000000\" LINK=\"#0033CC\" VLINK=\"#990099\" ALINK=\"#FF0000\">");
      lines.add(" </TABLE>");
      lines.add("      <FONT FACE=\"" + mainFont + ",Helvetica,Geneva,Sans-serif,sans-serif\">" + dirpage.getDescription() + "</FONT>");
      lines.add("      <P>");
      lines.add("      <TABLE WIDTH=\"100%\" BORDER=0 CELLSPACING=0 CELLPADDING=0 >");

      // write the arrows
      writeLinksToPages(lines, page);

      // write a helpful line of text if they've chosen to generate previews
      if (mainFrame.shouldWritePreviews())
      {
        lines.add("<center>Click on the image for full size image, or click on the \"preview\" for a smaller 1024x768 preview</center>");
      }

      // write all the dir headings to file.
      writeDirsToFile(lines);

      // now write all the pictures with links, too.
      writePicturesToFile(lines, page);
      lines.add("       </TABLE>");
      lines.add("</BODY>");
      lines.add("</HTML>");
      FileUtils.writeLines(file, lines);
    }
    catch (Exception e)
    {
      logger.error("Exception! ", e);
    }
  }

  /**  */
  private void writeLinksToPages(List<String> lines, int page) throws IOException
  {
    lines.add("          <TR>");
    lines.add("              <TD ALIGN=\"CENTER\">");
    lines.add("                  <TABLE BORDER=0  WIDTH=\"20%\" >");
    lines.add("                    <TR>");
    writePreviousLink(page, lines);

    if (mainFrame.shouldAddUpLink())
    {
      lines.add("              <TD><A HREF=\"../index.html\"><img src=\"./uparrow.gif\" border=\"0\"></A>");
    }

    writeNextLink(page, lines);
    lines.add("                     </TR>");
    lines.add("                   </TABLE>");
    lines.add("               </TD>");
    lines.add("           </TR>");
  }

  /**  */
  private void writePreviousLink(int page, List<String> lines) throws IOException
  {
    if (page == 1)
    {
      lines.add("              <TD><A HREF=\"./index.html\"><img src=\"./leftarrow.gif\" border=\"0\"></A>");
    }

    if (page > 1)
    {
      lines.add("              <TD><A HREF=\"./index" + (page - 1) + ".html\"><img src=\"./leftarrow.gif\" border=\"0\"></A>");
    }
  }

  /**  */
  private void writeNextLink(int page, List<String> lines) throws IOException
  {
    if (page < numPages)
    {
      lines.add("              <TD><A HREF=\"./index" + (page + 1) + ".html\"><img src=\"./rightarrow.gif\" border=\"0\"></A>");
    }
  }

  /**  */
  private void writeDirsToFile(List<String> lines) throws IOException
  {
    List<Dir> dirs = dirpage.getDirs();

    lines.add("          <TR>");
    lines.add("              <TD ALIGN=\"CENTER\">");
    lines.add("                  <TABLE BORDER=3 CELLSPACING=2 CELLPADDING=2 WIDTH=\"100%\" BORDERCOLOR=\"#808080\" BORDERCOLORDARK=\"#404040\" BORDERCOLORLIGHT=\"#C0C0C0\">");

    for (Dir dir : dirs)
    {
      String dirName = dir.getFile().getName();

      if (!"previews".equals(dirName))
      {
        lines.add("          <TR>");
        lines.add("              <TD width=\"50%\" ALIGN=\"CENTER\"><A HREF=\"./" + dirName + "/index.html\"><FONT FACE=\"" + dirFont.getFontName()
                    + "\"> " + dirName + "</FONT></A></TD>");

        String description = (dir.getDescription() == null) ? ""
                                                            : dir.getDescription();

        lines.add("              <TD width=\"50%\" ALIGN=\"LEFT\"><FONT FACE=\"                                        dirFont.getFontName() + \">"
                    + description + "</FONT>&nbsp;</TD>");
        lines.add("           </TR>");
      }
    }

    lines.add("                   </TABLE>");
    lines.add("               </TD>");
    lines.add("           </TR>");
  }

  /**  */
  private void writePicturesToFile(List<String> lines, int pageNumber) throws IOException
  {
    int startingRow = pageNumber * numRowsPerPage;
    int endRow      = (startingRow + numRowsPerPage) - 1;
    int maxRows     = (pics.size() / numColumns) + 1;

    endRow = Math.min(endRow, maxRows);
    Collections.sort(pics);
    lines.add("          <TR>");
    lines.add("              <TD ALIGN=\"CENTER\">");
    lines.add("              <FORM NAME=\"LAYOUTFORM\" ACTION=\"\" METHOD=POST>");
    lines.add("                  <TABLE BORDER=3 CELLSPACING=2 CELLPADDING=2 WIDTH=\"100%\" BORDERCOLOR=\"#808080\" BORDERCOLORDARK=\"#404040\" BORDERCOLORLIGHT=\"#C0C0C0\">");

    for (int rowNumber = startingRow; rowNumber <= endRow; rowNumber++)
    {
      writeRowOfImagesToFile(rowNumber, lines, pics);
    }

    lines.add("                   </TABLE>");
    lines.add("                   </FORM>");
    lines.add("               </TD>");
    lines.add("           </TR>");
  }

  /**  */
  private void writeRowOfImagesToFile(int rowNumber, List<String> lines, List<MediaFile> newPics) throws IOException
  {
    lines.add("                      <TR>");

    int picsSize = newPics.size();
    int width    = 100 / numColumns;

    for (int columnNumber = 0; columnNumber < numColumns; columnNumber++)
    {
      int elementNumber = (rowNumber * numColumns) + columnNumber;

      if (elementNumber < picsSize)
      {
        MediaFile pic = newPics.get(elementNumber);

        pic.writeImageElementToHtml(mainFrame, lines, width);
      }
      else
      {
        break;
      }
    }

    lines.add("                       </TR>");
  }
}
