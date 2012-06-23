package com.nurflugel.picturebrowserservlet.exif;

import java.io.Serializable;
import java.util.List;
import static com.nurflugel.picturebrowserservlet.exif.NurTagEnum.*;

public class NurTag implements Serializable
{
  private String            tagValue;
  private String            testValue;
  private static final long serialVersionUID = 7238105964141607868L;
  private NurTagEnum        tagType;

  public NurTag()
  {
    testValue = "       ";
  }

  public NurTag(String testValue)
  {
    this.testValue = testValue;
  }

  public NurTag(NurTagEnum tag, String tagText)
  {
    tagType   = tag;
    testValue = tag.toString();
    tagValue  = tagText;
    tagValue  = tagValue.substring(testValue.length());
  }

  public void setTagValue(String tagValue)
  {
    this.tagValue = tagValue;
  }

  public String getTagValue()
  {
    return tagValue;
  }

  public String getTestValue()
  {
    return testValue;
  }

  @Override
  public String toString()
  {
    return testValue.substring(7) + tagValue;
  }

  /**
   * Some cameras use "exposure time", some "shutter speed", some use F-Number, some use "aperture value" - this gets the right combo - F2.8 1:500
   * sec.
   */
  public static String getTopTag(List<NurTag> interestingTags)
  {
    String exposure = "";
    String fstop    = "";

    for (NurTag interestingTag : interestingTags)
    {
      int exposureTagIndex  = interestingTag.getTestValue().indexOf(ExposureTime.toString());
      int exposureTag2Index = interestingTag.getTestValue().indexOf(ShutterSpeed.toString());
      int fStopTagIndex     = interestingTag.getTestValue().indexOf(FStopNumber.toString());
      int apertureTagIndex  = interestingTag.getTestValue().indexOf(Aperature.toString());

      if (exposureTagIndex > -1)
      {
        exposure = interestingTag.getTagValue();
      }

      if (exposureTag2Index > -1)
      {
        exposure = interestingTag.getTagValue();
      }

      if (fStopTagIndex > -1)
      {
        fstop = interestingTag.getTagValue();
      }

      if (apertureTagIndex > -1)
      {
        fstop = interestingTag.getTagValue();
      }
    }

    return fstop + " @ " + exposure;
  }

  public NurTagEnum getTagType()
  {
    return tagType;
  }
}
