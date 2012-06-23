package com.nurflugel.picturebrowserservlet.htmlstuff;

/** Created by IntelliJ IDEA. User: Douglas Bullard Date: Jul 7, 2003 Time: 7:36:16 PM To change this template use Options | File Templates. */
public class Skin
{
  private String  backgroundFileName;
  private NurFont mainFont;
  private NurFont titleFont;
  private NurFont dirFont;
  private NurFont captionFont;

  /** Creates a new Skin object. */
  public Skin(String backgroundFileName, NurFont mainFont, NurFont titleFont, NurFont dirFont, NurFont captionFont)
  {
    this.backgroundFileName = backgroundFileName;
    this.mainFont           = mainFont;
    this.titleFont          = titleFont;
    this.dirFont            = dirFont;
    this.captionFont        = captionFont;
  }
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  public String getSkinName()
  {
    return getClass().getName();
  }
  // ------------------------ GETTER/SETTER METHODS ------------------------

  /**  */
  public String getBackgroundFileName()
  {
    return backgroundFileName;
  }

  /**  */
  public void setBackgroundFileName(String backgroundFileName)
  {
    this.backgroundFileName = backgroundFileName;
  }

  /**  */
  public NurFont getCaptionFont()
  {
    return captionFont;
  }

  /**  */
  public void setCaptionFont(NurFont captionFont)
  {
    this.captionFont = captionFont;
  }

  /**  */
  public NurFont getDirFont()
  {
    return dirFont;
  }

  /**  */
  public void setDirFont(NurFont dirFont)
  {
    this.dirFont = dirFont;
  }

  /**  */
  public String[] getFilesToCopy()
  {
    return new String[] { backgroundFileName };
  }

  /**  */
  public NurFont getMainFont()
  {
    return mainFont;
  }

  /**  */
  public void setMainFont(NurFont mainFont)
  {
    this.mainFont = mainFont;
  }

  /**  */
  public NurFont getTitleFont()
  {
    return titleFont;
  }

  /**  */
  public void setTitleFont(NurFont titleFont)
  {
    this.titleFont = titleFont;
  }
}
