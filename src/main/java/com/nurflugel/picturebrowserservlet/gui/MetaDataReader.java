package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import org.apache.log4j.Category;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import java.io.File;

/** Created by IntelliJ IDEA. User: Douglas Bullard Date: May 23, 2003 Time: 11:39:24 PM To change this template use Options | File Templates. */
public class MetaDataReader
{
  private static Category    logger              = LogFactory.getInstance(MetaDataReader.class);
  private File               currentDir;
  private String             metadataFileName;
  private Dir                dirpage;
  private boolean            shouldSetNumColumns;
  private int                numColumns;
  private int                numRows;
  private boolean            addUpLink;
  private MainFrameInterface mainFrame;

  /** Creates a new MetaDataReader object. */
  public MetaDataReader(MainFrameInterface mainFrame, File currentDir, String metadataFileName, Dir dirpage, boolean shouldSetNumColumns,
                        int numColumns, int numRows)
  {
    this.mainFrame           = mainFrame;
    this.currentDir          = currentDir;
    this.metadataFileName    = metadataFileName;
    this.dirpage             = dirpage;
    this.shouldSetNumColumns = shouldSetNumColumns;
    this.numColumns          = numColumns;
    this.numRows             = numRows;
  }

  /**  */
  public static boolean getBooleanFromTag(Element picElement, String tag)
  {
    String  textFromTag = getTextFromTag(picElement, tag);
    boolean result      = true;

    if ((textFromTag != null) && !textFromTag.isEmpty())
    {
      result = textFromTag.equalsIgnoreCase("true");
    }

    return result;
  }

  /**  */
  public static String getTextFromTag(Element element, String tag)
  {
    String  text       = null;
    Element subElement = element.getChild(tag);

    if (subElement != null)
    {
      text = subElement.getText();
    }

    text = (text == null) ? ""
                          : text;

    return text;
  }

  // ------------------------ OTHER METHODS ------------------------
  public void getInfoFromXmlFile()
  {
    try
    {
      // build the XML document from the file, get the root element, and the main components of the document
      SAXBuilder builder      = new SAXBuilder();
      File       metaDataFile = new File(currentDir, metadataFileName);

      if (metaDataFile.exists())
      {
        Document document    = builder.build(metaDataFile);
        Element  catalogRoot = document.getRootElement();

        dirpage.getPageInfo(catalogRoot, this);
      }
    }
    catch (Exception e)
    {
      logger.error("Error! ", e);
    }
  }

  /**  */
  public void setAddUpLink(boolean addUpLink)
  {
    this.addUpLink = addUpLink;
    mainFrame.setAddUpLink(addUpLink);
  }

  /**  */
  public void setNumColumnButtons(int numColumns)
  {
    mainFrame.setNumColumnButtons(numColumns);
  }

  /**  */
  public void setNumRows(int numRows)
  {
    this.numRows = numRows;
    mainFrame.setNumRowsDropdown(numRows);
  }

  // ------------------------ GETTER/SETTER METHODS ------------------------
  public MainFrameInterface getMainFrame()
  {
    return mainFrame;
  }

  /**  */
  public String getMetadataFileName()
  {
    return metadataFileName;
  }

  /**  */
  public int getNumColumns()
  {
    return numColumns;
  }

  /**  */
  public void setNumColumns(int numColumns)
  {
    this.numColumns = numColumns;
  }

  /**  */
  public int getNumRows()
  {
    return numRows;
  }

  /**  */
  public boolean isAddUpLink()
  {
    return addUpLink;
  }

  /**  */
  public boolean isShouldSetNumColumns()
  {
    return shouldSetNumColumns;
  }

  /**  */
  public void setShouldSetNumColumns(boolean shouldSetNumColumns)
  {
    this.shouldSetNumColumns = shouldSetNumColumns;
  }
}
