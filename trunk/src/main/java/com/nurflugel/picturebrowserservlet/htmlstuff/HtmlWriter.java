package com.nurflugel.picturebrowserservlet.htmlstuff;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.gui.TagsAndStuff;
import com.nurflugel.picturebrowserservlet.util.FilesToCopy;
import com.nurflugel.picturebrowserservlet.util.UtilMethods;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import com.nurflugel.picturebrowserservlet.gui.MainFrame;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Category;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.IMAGES_BASE;
import static com.nurflugel.picturebrowserservlet.util.UtilMethods.addToLines;
import static java.lang.Math.min;
import static java.util.Collections.sort;
import static org.apache.commons.io.FileUtils.copyFile;
import static org.apache.commons.io.FileUtils.writeLines;
import static org.apache.commons.lang.StringUtils.defaultString;

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
    String[] skinFilesToCopy = skin.getFilesToCopy();

    init();

    String systemFileName;

    // int            size              = systemFilesToCopy.length + skinFilesToCopy.length;
    // String[]       filesToCopy       = new String[size+20];
    for (FilesToCopy aSystemFilesToCopy : FilesToCopy.values())
    {
      systemFileName = "images/" + aSystemFilesToCopy;  // todo deal with these as resources...

      try
      {
        saveSystemImageFile(new File(systemFileName));
      }
      catch (IOException e)
      {
        System.out.println("Hiding exception, put printing stacktrace");
        e.printStackTrace();
      }
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

        if (logger.isDebugEnabled())
        {
          logger.debug("resource = " + resource);
        }

        if (resource == null)
        {
          logger.error("The resource is null for " + fileName);
        }
        else
        {
          saveMiscImageFiles(currentDir, fileName, resource);
        }
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
  private void saveMiscImageFiles(File currentDir, String fileName, URL resource) throws IOException
  {
    InputStream inputStream = resource.openStream();
    Writer      output      = null;

    if (logger.isDebugEnabled())
    {
      logger.debug("HtmlWriter.saveMiscImageFiles");
    }

    File destFile = new File(currentDir, fileName);

    try
    {
      output = new FileWriter(destFile);
      IOUtils.copy(inputStream, output);

      // FileUtils.copyFileToDirectory(file, currentDir);
    }
    catch (IOException e)
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("Error getting image", e);
      }
    }
    finally
    {
      output.close();
      inputStream.close();
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
      String       text = "<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">";

      addToLines(0, text, lines);
      addToLines(0, "<HTML>", lines);
      addToLines(0, "<HEAD>", lines);
      addToLines(0, "<META HTTP-EQUIV=\"Content-Type\" CONTENT=\"text/html; charset=iso-8859-1\">", lines);
      addToLines(0, "<META NAME=\"Generator\" CONTENT=\"BullaSoft PictureBrowserServlet\">", lines);
      addToLines(0, "<TITLE>index</TITLE>", lines);
      addToLines(0, "</HEAD>", lines);
      addPreBodyText(lines);
      addToLines(0,
                 "<BODY BGCOLOR=\"#FFFFFF\" BACKGROUND=\"./" + backgroundFileName
                   + "\" TEXT=\"#000000\" LINK=\"#0033CC\" VLINK=\"#990099\" ALINK=\"#FF0000\">", lines);

      // addLines(0, " </TABLE>", lines);
      addToLines(1, "<FONT FACE=\"" + mainFont + ",Helvetica,Geneva,Sans-serif,sans-serif\">" + dirpage.getDescription() + "</FONT>", lines);
      addToLines(1, "<P>", lines);
      addToLines(1, "<TABLE WIDTH=\"100%\" BORDER=0 CELLSPACING=0 CELLPADDING=0 >", lines);

      // write the arrows
      writeLinksToPages(lines, page);

      // write a helpful line of text if they've chosen to generate previews
      if (mainFrame.shouldWritePreviews())  // todo use preferences settings
      {
        addToLines(1, "<center>Click on the image for full size image, or click on the \"preview\" for a smaller 1024x768 preview</center>", lines);
      }

      // write all the dir headings to file.
      writeDirsToFile(lines);

      // now write all the pictures with links, too.
      writePicturesToFile(lines, page);
      addToLines(1, "</TABLE>", lines);
      addToLines(0, "</BODY>", lines);
      addPostBodyLines(lines);
      addToLines(0, "</HTML>", lines);
      writeLines(file, lines);
    }
    catch (Exception e)
    {
      logger.error("Exception! ", e);
    }
  }

  /** big todo to here - get customized Post BODY stuff here. */
  private void addPostBodyLines(List<String> lines)
  {
    addToLines(0, "<script type=\"text/javascript\">", lines);
    addToLines(0, "$(function() {", lines);
    addToLines(1, "    //Set the default directory to find the images needed", lines);
    addToLines(1, "    //by the plugin (closebtn.png, blank.gif, loading images ....)", lines);
    addToLines(1, "    $.fn.fancyzoom.defaultsOptions.imgDir='http://www.nurflugel.com/images/'; //very important must finish with a /", lines);
    addToLines(0, "", lines);
    addToLines(1, "    // Select all links in object with gallery ID using the defaults options", lines);
    addToLines(1, "    $('#gallery a').fancyzoom(); ", lines);
    addToLines(0, "", lines);
    addToLines(1, "    // Select all links with tozoom class, set the open animation time to 1000", lines);
    addToLines(1, "    $('a.tozoom').fancyzoom({Speed:1000});", lines);
    addToLines(0, "", lines);
    addToLines(1, "    // Select all links set the overlay opacity to 80%", lines);
    addToLines(1, "    $('a').fancyzoom({overlay:0.1});", lines);
    addToLines(1, "    ", lines);
    addToLines(1, "    //new rev > 1.2", lines);
    addToLines(1, "    //apply fancyzoom effect on all image whose class is fancyzoom !!", lines);
    addToLines(1, "    $('img.fancyzoom').fancyzoom();", lines);
    addToLines(0, "", lines);
    addToLines(0, "", lines);
    addToLines(0, "});", lines);
    addToLines(0, "</script>", lines);
  }

  private void addPreBodyText(List<String> lines)
  {
    addToLines(0, "", lines);
    addToLines(0, "<script type=\"text/javascript\" src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.6.4/jquery.min.js\"></script>", lines);
    addToLines(0, "", lines);
    addToLines(0, "<!-- //optional -->", lines);
    addToLines(0, "<script type=\"text/javascript\" src=\"http://www.nurflugel.com/js/jquery.shadow.js\"></script>", lines);
    addToLines(0, "<script type=\"text/javascript\" src=\"http://www.nurflugel.com/js/jquery.ifixpng.js\"></script>", lines);
    addToLines(0, "<!-- //the plugin itself -->", lines);
    addToLines(0, "<script type=\"text/javascript\" src=\"http://www.nurflugel.com/js/jquery.fancyzoom.js\"></script>\n", lines);
  }

  /**  */
  private void writeLinksToPages(List<String> lines, int page)
  {
    addToLines(2, "<TR>", lines);
    addToLines(3, "<TD ALIGN=\"CENTER\">", lines);
    addToLines(4, "<TABLE BORDER=0  WIDTH=\"20%\" >", lines);
    addToLines(5, "<TR>", lines);
    writePreviousLink(page, lines);

    if (mainFrame.shouldAddUpLink())
    {
      addToLines(6, "<TD><A HREF=\"../index.html\"><img src=\"" + IMAGES_BASE + "uparrow.gif\" border=\"0\"></A>", lines);
    }

    writeNextLink(page, lines);
    addToLines(5, "</TR>", lines);
    addToLines(4, "</TABLE>", lines);
    addToLines(3, "</TD>", lines);
    addToLines(2, "</TR>", lines);
  }

  /**  */
  private void writePreviousLink(int page, List<String> lines)
  {
    if (page == 1)
    {
      // addToLines(5, "<TD><A HREF=\"./index.html\"><img src=\"./leftarrow.gif\" border=\"0\"></A>", lines);
      addToLines(5, "<TD><A HREF=\"./index.html\"><img src=\"" + IMAGES_BASE + "leftarrow.gif\" border=\"0\"></A>", lines);
    }

    if (page > 1)
    {
      addToLines(5, "<TD><A HREF=\"./index" + (page - 1) + ".html\"><img src=\"" + IMAGES_BASE + "leftarrow.gif\" border=\"0\"></A>", lines);
    }
  }

  /**  */
  private void writeNextLink(int page, List<String> lines)
  {
    if (page < numPages)
    {
      addToLines(4, "<TD><A HREF=\"./index" + (page + 1) + ".html\"><img src=\"" + IMAGES_BASE + "rightarrow.gif\" border=\"0\"></A>", lines);
    }
  }

  /**  */
  private void writeDirsToFile(List<String> lines)
  {
    List<Dir> dirs = dirpage.getDirs();

    addToLines(2, "<TR>", lines);
    addToLines(3, "<TD ALIGN=\"CENTER\">", lines);
    addToLines(4,
               "<TABLE BORDER=3 CELLSPACING=2 CELLPADDING=2 WIDTH=\"100%\" BORDERCOLOR=\"#808080\" BORDERCOLORDARK=\"#404040\" BORDERCOLORLIGHT=\"#C0C0C0\">",
               lines);

    for (Dir dir : dirs)
    {
      String dirName = dir.getFile().getName();

      if (!"previews".equals(dirName))
      {
        addToLines(5, "<TR>", lines);
        addToLines(6,
                   "<TD width=\"50%\" ALIGN=\"CENTER\"><A HREF=\"./" + dirName + "/index.html\"><FONT FACE=\"" + dirFont.getFontName() + "\"> "
                     + dirName + "</FONT></A></TD>", lines);

        String description = defaultString(dir.getDescription(), "");

        addToLines(6, "<TD width=\"50%\" ALIGN=\"LEFT\"><FONT FACE=\"" + dirFont.getFontName() + "\">" + description + "</FONT>&nbsp;</TD>", lines);
        addToLines(5, "</TR>", lines);
      }
    }

    addToLines(4, "</TABLE>", lines);
    addToLines(3, "</TD>", lines);
    addToLines(2, "</TR>", lines);
  }

  /**  */
  private void writePicturesToFile(List<String> lines, int pageNumber) throws IOException
  {
    int startingRow = pageNumber * numRowsPerPage;
    int endRow      = (startingRow + numRowsPerPage) - 1;
    int maxRows     = (pics.size() / numColumns) + 1;
    int indent      = 0;

    endRow = min(endRow, maxRows);
    sort(pics);
    addToLines(++indent, "<TR>", lines);
    addToLines(++indent, "<TD ALIGN=\"CENTER\">", lines);
    addToLines(++indent, "<FORM NAME=\"LAYOUTFORM\" ACTION=\"\" METHOD=POST>", lines);
    addToLines(++indent,
               "<TABLE BORDER=3 CELLSPACING=2 CELLPADDING=2 WIDTH=\"100%\" BORDERCOLOR=\"#808080\" BORDERCOLORDARK=\"#404040\" BORDERCOLORLIGHT=\"#C0C0C0\">",
               lines);

    for (int rowNumber = startingRow; rowNumber <= endRow; rowNumber++)
    {
      writeRowOfImagesToFile(rowNumber, lines, pics, ++indent);
    }

    addToLines(--indent, "</TABLE>", lines);
    addToLines(--indent, "</FORM>", lines);
    addToLines(--indent, "</TD>", lines);
    addToLines(--indent, "</TR>", lines);
  }

  /**  */
  private void writeRowOfImagesToFile(int rowNumber, List<String> lines, List<MediaFile> newPics, int indent) throws IOException
  {
    addToLines(++indent, "<TR>", lines);

    int picsSize = newPics.size();
    int width    = 100 / numColumns;

    for (int columnNumber = 0; columnNumber < numColumns; columnNumber++)
    {
      int elementNumber = (rowNumber * numColumns) + columnNumber;

      if (elementNumber < picsSize)
      {
        MediaFile pic = newPics.get(elementNumber);

        pic.writeImageElementToHtml(mainFrame, lines, width, ++indent);
      }
      else
      {
        break;
      }
    }

    addToLines(--indent, "</TR>", lines);
  }
}
