package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.gui.MainFrame;
import com.nurflugel.picturebrowserservlet.gui.MetaDataReader;
import com.nurflugel.picturebrowserservlet.gui.MetadataWriter;
import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.*;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import java.io.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Dir extends com.nurflugel.picturebrowserservlet.domain.File
{
  /** Use serialVersionUID for interoperability. */
  private static final long serialVersionUID    = -2903034968725770116L;
  private String            introText;
  private List<Dir>         dirs;
  private List<MediaFile>   pics;
  private boolean           displayExifDropdown = true;

  /** Creates a new Dir object. */
  public Dir(File file, String title)
  {
    super(file, title);
  }

  /** Creates a new Dir object. */
  public Dir(File file, String title, String description, String url, String introText, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
    this.introText = introText;
  }

  public Dir(File file, String title, SortCriteria sortCriteria)
  {
    super(file, title, sortCriteria);
  }

  /**  */
  public void getPageInfo(Element catalogRoot, MetaDataReader metadataReader)
  {
    Element dirElement = catalogRoot.getChild(titleTag);
    String  pageTitle  = dirElement.getText();

    setTitle(pageTitle);
    displayExifDropdown = MetaDataReader.getBooleanFromTag(catalogRoot, displayExifDropdownTag);

    if (metadataReader.isShouldSetNumColumns())
    {
      String numcolumnsText = MetaDataReader.getTextFromTag(catalogRoot, numColumnsTag);
      int    numColumns     = StringUtils.isBlank(numcolumnsText) ? MainFrame.DEFAULT_NUM_COLUMNS
                                                                  : new Integer(numcolumnsText);

      metadataReader.setNumColumnButtons(numColumns);
    }

    Element numRowsElement = catalogRoot.getChild(numRowsTag);
    int     numRows;

    if (numRowsElement != null)  // todo numberUtils
    {
      String numRowsText = numRowsElement.getText();

      numRows = new Integer(numRowsText);
    }
    else
    {
      numRows = 4;
    }

    metadataReader.setNumRows(numRows);

    Element addUpLinkElement = catalogRoot.getChild(addUpLinkToPageTag);

    if (addUpLinkElement == null)
    {
      metadataReader.setAddUpLink(true);
    }
    else
    {
      String addUpLinkText = addUpLinkElement.getText();

      metadataReader.setAddUpLink("TRUE".equalsIgnoreCase(addUpLinkText));
    }

    dirElement = catalogRoot.getChild(descriptionTag);

    String description = dirElement.getText();

    setDescription(description);
    getPics(catalogRoot);
    getDirs(catalogRoot);
  }

  /**  */
  public void setDisplayExifDropdown(boolean displayExifDropdown)
  {
    this.displayExifDropdown = displayExifDropdown;
  }

  /**  */
  private void getPics(Element catalogRoot)
  {
    Element picsElement = catalogRoot.getChild(picsTag);
    List    picElements = picsElement.getChildren(picTagElement);

    for (Object picElement1 : picElements)
    {
      Element picElement            = (Element) picElement1;
      String  fileName              = MetaDataReader.getTextFromTag(picElement, filenameTag);
      String  title                 = MetaDataReader.getTextFromTag(picElement, titleTag);
      String  description           = MetaDataReader.getTextFromTag(picElement, descriptionTag);
      String  url                   = MetaDataReader.getTextFromTag(picElement, urlTag);
      String  tagText               = MetaDataReader.getTextFromTag(picElement, disPlaySequenceNumberTag);
      int     displaySequenceNumber;
      boolean displayExifDropdown   = MetaDataReader.getBooleanFromTag(picElement, displayExifDropdownTag);

      try
      {
        displaySequenceNumber = Integer.parseInt(tagText);
      }
      catch (NumberFormatException e)
      {
        displaySequenceNumber = DEFAULT_DISPLAY_SEQUENCE_NUMBER;
      }

      findAndSetMatchingPic(fileName, title, description, url, displaySequenceNumber, displayExifDropdown);
    }
  }

  /**  */
  public void findAndSetMatchingPic(String fileName, String title, String description, String url, int displaySequenceNumber,
                                    boolean shouldDisplayExifDropdown)
  {
    // go through the thePics to find a matching file name
    for (MediaFile pic : pics)
    {
      String name = pic.getFile().getName();

      if (name.equals(fileName))
      {
        pic.setTitle(title);
        pic.setDescription(description);
        pic.setUrl(url);
        pic.setDisplaySequenceNumber(displaySequenceNumber);
        pic.setDisplayExifDropdown(shouldDisplayExifDropdown);

        if (pic instanceof Jpg)
        {
          pic.setDisplayExifDropdown(shouldDisplayExifDropdown);
        }
      }
    }
  }

  /**  */
  private void getDirs(Element catalogRoot)
  {
    Element dirsElement = catalogRoot.getChild(dirsTag);
    List    dirElements = dirsElement.getChildren(dirTagElement);

    for (Object dirElement1 : dirElements)
    {
      Element dirElement            = (Element) dirElement1;
      String  fileName              = MetaDataReader.getTextFromTag(dirElement, filenameTag);
      String  title                 = MetaDataReader.getTextFromTag(dirElement, titleTag);
      String  description           = MetaDataReader.getTextFromTag(dirElement, descriptionTag);
      String  url                   = MetaDataReader.getTextFromTag(dirElement, urlTag);
      String  numberText            = MetaDataReader.getTextFromTag(dirElement, disPlaySequenceNumberTag);
      int     displaySequenceNumber;

      try
      {
        displaySequenceNumber = Integer.parseInt(numberText);
      }
      catch (NumberFormatException e)
      {
        displaySequenceNumber = DEFAULT_DISPLAY_SEQUENCE_NUMBER;
      }

      findAndSetMatchingDir(fileName, title, description, url, displaySequenceNumber, this);
    }
  }

  /**  */
  public void findAndSetMatchingDir(String fileName, String title, String description, String url, int displaySequenceNumber, Dir dirpage)
  {   // go through the pics to find a matching file name

    List<Dir> dirPageDirs = dirpage.getDirs();

    for (Dir dir : dirPageDirs)
    {
      if (dir.getFile().getName().equals(fileName))
      {
        dir.setTitle(title);
        dir.setDescription(description);
        dir.setUrl(url);
        dir.setDisplaySequenceNumber(displaySequenceNumber);
      }
    }
  }

  /**  */
  public List<Dir> getDirs()
  {
    return (dirs == null) ? new ArrayList<Dir>()
                          : new ArrayList<Dir>(dirs);
  }

  /**  */
  @Override
  public void getXmlElementFromDomainObject(Element header)
  {
    Element dirTag = new Element(dirTagElement);

    header.addContent(dirTag);

    String text = getFile().getName();

    MetadataWriter.addItem(dirTag, filenameTag, text);
    text = getDescription();
    MetadataWriter.addItem(dirTag, descriptionTag, text);
    MetadataWriter.addItem(dirTag, titleTag, getTitle());
    MetadataWriter.addItem(dirTag, urlTag, getUrl());
    MetadataWriter.addItem(dirTag, disPlaySequenceNumberTag, getDisplaySequenceNumber());
  }

  /**  */
  public void remove(MediaFile thePic)
  {
    pics.remove(thePic);
  }

  /**  */
  public void saveBasicPage(Element root, Dir dirpage, int numColumns, int numRows, boolean addUpLink)
  {
    String  description = dirpage.getDescription();
    String  introText   = dirpage.introText;
    String  title       = dirpage.getTitle();
    Element subElement  = new Element(descriptionTag, description);

    root.addContent(subElement);

    String displayExifText = displayExifDropdown ? "true"
                                                 : "false";

    subElement = new Element(displayExifDropdownTag);
    subElement.setText(displayExifText);
    root.addContent(subElement);
    subElement = new Element(introTag, introText);
    root.addContent(subElement);
    subElement = new Element(titleTag, title);
    root.addContent(subElement);
    subElement = new Element(numColumnsTag);
    subElement.setText(String.valueOf(numColumns));
    root.addContent(subElement);
    subElement = new Element(numRowsTag);
    subElement.setText(String.valueOf(numRows));
    root.addContent(subElement);
    subElement = new Element(addUpLinkToPageTag);

    if (addUpLink)
    {
      subElement.setText("true");
    }
    else
    {
      subElement.setText("false");
    }

    root.addContent(subElement);
  }

  /**  */
  public void setDirs(List<Dir> dirs)
  {
    this.dirs = new ArrayList<Dir>();
    this.dirs.addAll(dirs);
  }

  /**  */
  public void setPics(List<MediaFile> pics)
  {
    this.pics = new ArrayList<MediaFile>();
    this.pics.addAll(pics);
  }
  // ------------------------ GETTER/SETTER METHODS ------------------------

  /**  */
  public String getIntroText()
  {
    return introText;
  }

  /**  */
  public void setIntroText(String introText)
  {
    this.introText = introText;
  }
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  public boolean getDefaultDisplayExifDropdown()
  {
    return displayExifDropdown;
  }

  /**  */
  public List<MediaFile> getPics()
  {
    return new ArrayList<MediaFile>(pics);
  }
}
