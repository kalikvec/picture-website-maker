/*
 * Created by dnoakes on 26-Nov-2002 01:26:39 using IntelliJ IDEA.
 */
package com.drew.metadata.iptc;

import com.drew.metadata.Directory;
import java.util.HashMap;

/**  */
public class IptcDirectory extends Directory
{
  public static final int                         TAG_RECORD_VERSION                  = 0x0200;
  public static final int                         TAG_CAPTION                         = 0x0278;
  public static final int                         TAG_WRITER                          = 0x027a;
  public static final int                         TAG_HEADLINE                        = 0x0269;
  public static final int                         TAG_SPECIAL_INSTRUCTIONS            = 0x0228;
  public static final int                         TAG_BY_LINE                         = 0x0250;
  public static final int                         TAG_BY_LINE_TITLE                   = 0x0255;
  public static final int                         TAG_CREDIT                          = 0x026e;
  public static final int                         TAG_SOURCE                          = 0x0273;
  public static final int                         TAG_OBJECT_NAME                     = 0x0205;
  public static final int                         TAG_DATE_CREATED                    = 0x0237;
  public static final int                         TAG_CITY                            = 0x025a;
  public static final int                         TAG_PROVINCE_OR_STATE               = 0x025f;
  public static final int                         TAG_COUNTRY_OR_PRIMARY_LOCATION     = 0x0265;
  public static final int                         TAG_ORIGINAL_TRANSMISSION_REFERENCE = 0x0267;
  public static final int                         TAG_CATEGORY                        = 0x020f;
  public static final int                         TAG_SUPPLEMENTAL_CATEGORIES         = 0x0214;
  public static final int                         TAG_URGENCY                         = 0x0200 | 10;
  public static final int                         TAG_KEYWORDS                        = 0x0200 | 25;
  public static final int                         TAG_COPYRIGHT_NOTICE                = 0x0274;
  public static final int                         TAG_RELEASE_DATE                    = 0x0200 | 30;
  public static final int                         TAG_RELEASE_TIME                    = 0x0200 | 35;
  public static final int                         TAG_TIME_CREATED                    = 0x0200 | 60;
  public static final int                         TAG_ORIGINATING_PROGRAM             = 0x0200 | 65;
  protected static final HashMap<Integer, String> tagNameMap                          = new HashMap<Integer, String>();

  static
  {
    tagNameMap.put(TAG_RECORD_VERSION, "Directory Version");
    tagNameMap.put(TAG_CAPTION, "Caption/Abstract");
    tagNameMap.put(TAG_WRITER, "Writer/Editor");
    tagNameMap.put(TAG_HEADLINE, "Headline");
    tagNameMap.put(TAG_SPECIAL_INSTRUCTIONS, "Special Instructions");
    tagNameMap.put(TAG_BY_LINE, "By-line");
    tagNameMap.put(TAG_BY_LINE_TITLE, "By-line Title");
    tagNameMap.put(TAG_CREDIT, "Credit");
    tagNameMap.put(TAG_SOURCE, "Source");
    tagNameMap.put(TAG_OBJECT_NAME, "Object Name");
    tagNameMap.put(TAG_DATE_CREATED, "Date Created");
    tagNameMap.put(TAG_CITY, "City");
    tagNameMap.put(TAG_PROVINCE_OR_STATE, "Province/State");
    tagNameMap.put(TAG_COUNTRY_OR_PRIMARY_LOCATION, "Country/Primary Location");
    tagNameMap.put(TAG_ORIGINAL_TRANSMISSION_REFERENCE, "Original Transmission Reference");
    tagNameMap.put(TAG_CATEGORY, "Category");
    tagNameMap.put(TAG_SUPPLEMENTAL_CATEGORIES, "Supplemental Category(s)");
    tagNameMap.put(TAG_URGENCY, "Urgency");
    tagNameMap.put(TAG_KEYWORDS, "Keywords");
    tagNameMap.put(TAG_COPYRIGHT_NOTICE, "Copyright Notice");
    tagNameMap.put(TAG_RELEASE_DATE, "Release Date");
    tagNameMap.put(TAG_RELEASE_TIME, "Release Time");
    tagNameMap.put(TAG_TIME_CREATED, "Time Created");
    tagNameMap.put(TAG_ORIGINATING_PROGRAM, "Originating Program");
  }

  public IptcDirectory()
  {
    setDescriptor(new IptcDescriptor(this));
  }

  public String getName()
  {
    return "Iptc";
  }

  protected HashMap getTagNameMap()
  {
    return tagNameMap;
  }
}
