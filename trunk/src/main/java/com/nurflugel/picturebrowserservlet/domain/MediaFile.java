package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.gui.MainFrame;
import com.nurflugel.picturebrowserservlet.gui.MetadataWriter;
import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.*;
import com.nurflugel.picturebrowserservlet.gui.ThumbnailReaderWriter;
import com.nurflugel.picturebrowserservlet.htmlstuff.NurFont;
import org.apache.log4j.Category;
import org.jdom.Element;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class MediaFile extends com.nurflugel.picturebrowserservlet.domain.File
{
  private static final Category logger                = LogFactory.getInstance(MediaFile.class);
  private boolean               displayExifDropdown   = true;
  private ThumbnailReaderWriter thumbnailReaderWriter;

  /** Creates a new MediaFile object. */
  protected MediaFile(File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);

    if (logger.isDebugEnabled())
    {
      logger.debug("Media file constructor");
    }

    displayExifDropdown   = true;
    thumbnailReaderWriter = MainFrame.getThumbnailHandler();
  }

  /** Creates the XML representation. */
  @Override
  public void getXmlElementFromDomainObject(Element header)
  {
    Element picTag = new Element(picTagElement);

    header.addContent(picTag);
    MetadataWriter.addItem(picTag, filenameTag, getFile().getName());
    MetadataWriter.addItem(picTag, descriptionTag, getDescription());
    MetadataWriter.addItem(picTag, titleTag, getTitle());
    MetadataWriter.addItem(picTag, urlTag, getUrl());
    MetadataWriter.addItem(picTag, disPlaySequenceNumberTag, getDisplaySequenceNumber());

    // if (displayExifDropdown)
    // {
    MetadataWriter.addItem(picTag, displayExifDropdownTag, displayExifDropdown);

    // }
  }

  public boolean shouldDisplayExifDropdown()
  {
    return displayExifDropdown;
  }

  /** Creates the HTML representation. */
  public void writeImageElementToHtml(MainFrame mainFrame, List<String> lines, int width) throws IOException
  {
    File    file        = getFile();
    String  description = getDescription();
    String  fileName    = file.getName();
    NurFont mainFont    = mainFrame.getSkin().getMainFont();
    String  fontName    = mainFont.getFontName();

    lines.add("                          <TD width=\"" + width + "%\" VALIGN=MIDDLE ALIGN=CENTER >");
    lines.add("                              <P ALIGN=CENTER>\n");  // +
    lines.add("<A HREF=\"" + fileName + "\"><IMG SRC=\"./" + getThumbnailName() + "\" ALIGN=\"BOTTOM\" BORDER=0 ALT=\"" + fileName
                + "\"></A><BR><FONT FACE=\"" + fontName + "\">" + fileName + "</FONT>");

    if (mainFrame.shouldWritePreviews())
    {
      lines.add("<A HREF=\"" + "previews/" + file.getName() + "\"> preview</A><br>");
    }
    else
    {
      lines.add("<br>\n");
    }

    boolean showExifInfo = mainFrame.showExifInfoCheckBox();

    if (showExifInfo && displayExifDropdown)
    {
      lines.add("                                   " + getDropdownHtml() + " <BR>");
    }

    lines.add("                                   " + description + " <BR>");
    lines.add("                           </TD>");
  }

  protected String getThumbnailName()
  {
    return VIDEO_ICON;
  }

  String getDropdownHtml()
  {
    return "";
  }

  public ThumbnailReaderWriter getThumbnailReaderWriter()
  {
    return thumbnailReaderWriter;
  }

  public void setThumbnailReaderWriter(ThumbnailReaderWriter thumbnailReaderWriter)
  {
    this.thumbnailReaderWriter = thumbnailReaderWriter;
  }

  public boolean isDisplayExifDropdown()
  {
    return displayExifDropdown;
  }

  public void setDisplayExifDropdown(boolean displayExifDropdown)
  {
    this.displayExifDropdown = displayExifDropdown;
  }
}
