package com.nurflugel.picturebrowserservlet.htmlstuff;

import java.awt.*;

/** Created by IntelliJ IDEA. User: Douglas Bullard Date: Jul 7, 2003 Time: 10:59:12 PM To change this template use Options | File Templates. */
public class NurFont
{
  private Font   theFont;
  private String fontName;
  private String fontSize;

  /** Creates a new NurFont object. */
  public NurFont(Font theFont, String fontName, String fontSize)
  {
    this.theFont  = theFont;
    this.fontName = fontName;
    this.fontSize = fontSize;
  }

  // ------------------------ GETTER/SETTER METHODS ------------------------
  public String getFontName()
  {
    return fontName;
  }

  /**  */
  public void setFontName(String fontName)
  {
    this.fontName = fontName;
  }

  /**  */
  public String getFontSize()
  {
    return fontSize;
  }

  /**  */
  public void setFontSize(String fontSize)
  {
    this.fontSize = fontSize;
  }

  /**  */
  public Font getTheFont()
  {
    return theFont;
  }

  /**  */
  public void setTheFont(Font theFont)
  {
    this.theFont = theFont;
  }
}
