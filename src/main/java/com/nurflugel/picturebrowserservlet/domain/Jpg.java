package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.exif.NurTag;
import com.nurflugel.picturebrowserservlet.exif.NurTagEnum;
import com.nurflugel.picturebrowserservlet.exif.NurTagFilter;
import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import java.io.File;
import java.util.List;

public class Jpg extends GraphicFile
{
  public Jpg(File file, String title, String description, String url, boolean displayExifDropdown, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
    setDisplayExifDropdown(displayExifDropdown);
  }

  // todo when renumbering display sequences, then use this wierd algoritym, ele
  public int compareTo(Object o)
  {
    if (o instanceof Jpg)
    {
      Jpg otherJpg               = (Jpg) o;
      int displaySequenceNumber  = getDisplaySequenceNumber();
      int displaySequenceNumber2 = otherJpg.getDisplaySequenceNumber();

      if ((displaySequenceNumber == INITIAL_SEQUENCE_NUMBER) && (displaySequenceNumber2 == INITIAL_SEQUENCE_NUMBER))
      {
        if (sortCriteria == SortCriteria.ImageTimestamp)
        {
          String date1 = getOriginalDate();
          String date2 = otherJpg.getOriginalDate();

          return date1.compareTo(date2);
        }
      }

      // return super.compareTo(o);
    }
    // } else if (sortCriteria == SortCriteria.FileName) {
    //
    // if (o instanceof File) {
    // File file1 = (File) o;
    //
    // String thisName  = getFile().getName();
    // String otherName = file1.getFile().getName();
    //
    // return thisName.compareTo(otherName);
    // }
    // }
    // if (o instanceof File) {
    // File file1                  = (File) o;
    // int  displaySequenceNumber  = getDisplaySequenceNumber();
    // int  displaySequenceNumber2 = file1.getDisplaySequenceNumber();
    //
    // if ((displaySequenceNumber != INITIAL_SEQUENCE_NUMBER) && (displaySequenceNumber2 != INITIAL_SEQUENCE_NUMBER)) {
    // return super.compareTo(o);
    // }
    // }

    // else
    return super.compareTo(o);
  }

  private String getOriginalDate()
  {
    List<NurTag> tags = getInterestingTags();

    for (NurTag tag : tags)
    {
      if (tag.getTagType() == NurTagEnum.DateTime)
      {
        return tag.getTagValue();
      }
    }

    return "";
  }

  private List<NurTag> getInterestingTags()
  {
    NurTagFilter nurTagFilter = new NurTagFilter(getFile());

    return nurTagFilter.getInterestingTags();
  }

  @SuppressWarnings({ "RefusedBequest" })
  public String getDropdownHtml()
  {
    StringBuilder text = new StringBuilder();

    if (isDisplayExifDropdown())
    {
      List<NurTag> interestingTags = getInterestingTags();
      String       topTag          = NurTag.getTopTag(interestingTags);

      if (interestingTags.size() > 4)
      {
        text.append("<SELECT >\n" + "                        <OPTION VALUE=\"EXIF Info\">\n").append(topTag).append("\n");
        text.append("                        </OPTION>\n");

        for (NurTag interestingTag : interestingTags)
        {
          text.append("<option value=\"").append(interestingTag.getTestValue()).append("\">\n");

          String displayText = interestingTag.getTestValue() + interestingTag.getTagValue();
          int    beginIndex  = displayText.indexOf("[Exif] ");

          displayText = displayText.substring(beginIndex + 7);
          text.append(displayText).append(" </OPTION>\n");
        }

        text.append(" </SELECT>");
      }
    }

    return text.toString();
  }

  @SuppressWarnings({ "RefusedBequest" })
  protected String getThumbnailName()
  {
    return getThumbnailReaderWriter().getThumbnailFileName(getFile());
  }
}
