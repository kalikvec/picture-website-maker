package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import org.apache.log4j.Category;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class MetadataWriter
{
  private static Category logger = LogFactory.getInstance(MetadataWriter.class);

  private MetadataWriter() {}

  /**  */
  public static void saveMetadata(File currentDir, Dir dirpage, int numColumns, int numRows, boolean addUpLink)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Currenr dir is " + currentDir);
    }

    if (currentDir != null)
    {
      XMLOutputter outputter = new XMLOutputter();
      Element      root      = new Element(TagsAndStuff.metadataTag);
      Document     document  = new Document(root);

      dirpage.saveBasicPage(root, dirpage, numColumns, numRows, addUpLink);
      saveDirs(root, dirpage);
      saveMediaFiles(root, dirpage);

      FileOutputStream fileOutputStream = null;

      try
      {
        // outputter.setEncoding("UTF-8");
        // outputter.setIndentSize(4);
        // outputter.setNewlines(true);
        // outputter.setOmitDeclaration(false);
        // outputter.setOmitEncoding(false);
        outputter.setFormat(Format.getPrettyFormat());

        File file = new File(currentDir, TagsAndStuff.metadataFileName);

        if (logger.isDebugEnabled())
        {
          logger.debug("MetadataWriter.saveMetadata::file = " + file.getAbsolutePath());
        }

        fileOutputStream = new FileOutputStream(file);
        outputter.output(document, fileOutputStream);
      }
      catch (FileNotFoundException e)
      {
        logger.error("e = ", e);
      }
      catch (IOException e)
      {
        logger.error("e=", e);
      }
      finally
      {
        try
        {
          assert fileOutputStream != null;
          fileOutputStream.close();
        }  // todo used to catch NPE here...
        catch (IOException e)
        {
          logger.error("e=", e);
        }
      }
    }
  }

  /**  */
  private static void saveDirs(Element root, Dir dirpage)
  {
    Element header = new Element(TagsAndStuff.dirsTag);

    root.addContent(header);

    List<Dir> dirs = dirpage.getDirs();

    for (Dir dir : dirs)
    {
      dir.getXmlElementFromDomainObject(header);
    }
  }

  /**  */
  private static void saveMediaFiles(Element root, Dir dirpage)
  {
    Element header = new Element(TagsAndStuff.picsTag);

    root.addContent(header);

    List<MediaFile> pics = dirpage.getPics();

    for (MediaFile pic : pics)
    {
      pic.getXmlElementFromDomainObject(header);
    }
  }

  /**  */
  public static void addItem(Element picTag, String tagName, boolean showExifDropdown)
  {
    Element subElement = new Element(tagName);

    subElement.setText(showExifDropdown ? "true"
                                        : "false");
    picTag.addContent(subElement);
  }

  /** Helper method to add an item to the document. */
  public static void addItem(Element root, String tagName, int tagValue)
  {
    Element subElement = new Element(tagName);

    subElement.setText(tagValue + "");
    root.addContent(subElement);
  }

  /** Helper method to add an item to the document. */
  public static void addItem(Element root, String tagName, String tagValue)
  {
    Element subElement = new Element(tagName);

    subElement.setText(tagValue);
    root.addContent(subElement);
  }
}
