package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import org.apache.log4j.Category;
import org.jdom.Element;
import java.io.Serializable;

public class File implements Comparable, Serializable
{
  private java.io.File       file;
  private String             title;
  private String             description;
  private String             url;
  private int                displaySequenceNumber   = INITIAL_SEQUENCE_NUMBER;
  private static final long  serialVersionUID        = -6100007254434481040L;
  protected SortCriteria     sortCriteria            = SortCriteria.FileName;
  protected static final int INITIAL_SEQUENCE_NUMBER = 999999;
  private static Category    logger                  = LogFactory.getInstance(File.class);

  public File(java.io.File file, String title, SortCriteria sortCriteria)
  {
    this(file, title);
    this.sortCriteria = sortCriteria;
  }

  protected File(java.io.File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    this.file         = file;
    this.title        = title;
    this.description  = description;
    this.url          = url;
    this.sortCriteria = sortCriteria;
  }

  public File(java.io.File file, String title)
  {
    this.file  = file;
    this.title = title;
  }

  void getXmlElementFromDomainObject(Element header) {}

  public int compareTo(Object o)
  {
    int result = 0;

    try
    {
      File otherFile = (File) o;
      int  firstId   = getDisplaySequenceNumber();
      int  secondId  = otherFile.getDisplaySequenceNumber();

      // first, the case of arbitrary sort orders
      if ((firstId != INITIAL_SEQUENCE_NUMBER) && (secondId != INITIAL_SEQUENCE_NUMBER))
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("both sequence numbers were not default");
        }

        result = firstId - secondId;

        return result;
      }
      // else if (sortCriteria == SortCriteria.FileName) {
      // return file.getName().compareTo(otherFile.getFile().getName());
      // }
    }
    catch (Exception e) {}

    return result;
  }

  public int getDisplaySequenceNumber()
  {
    return displaySequenceNumber;
  }

  public void setDisplaySequenceNumber(int displaySequenceNumber)
  {
    this.displaySequenceNumber = displaySequenceNumber;
  }

  @Override
  public String toString()
  {
    return file.getAbsolutePath();
  }

  public java.io.File getFile()
  {
    return file;
  }

  public void setFile(java.io.File file)
  {
    this.file = file;
  }

  public String getTitle()
  {
    return title;
  }

  public void setTitle(String title)
  {
    this.title = title;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public String getUrl()
  {
    return url;
  }

  public void setUrl(String url)
  {
    this.url = url;
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }

    if ((o == null) || (getClass() != o.getClass()))
    {
      return false;
    }

    File file1 = (File) o;

    if (!file.equals(file1.file))
    {
      return false;
    }

    return true;
  }

  @Override
  public int hashCode()
  {
    return file.hashCode();
  }
}
