package com.nurflugel.picturebrowserservlet.htmlstuff;

import com.nurflugel.picturebrowserservlet.gui.TagsAndStuff;
import java.awt.*;

/** Created by IntelliJ IDEA. User: Douglas Bullard Date: Jul 7, 2003 Time: 7:47:28 PM To change this template use Options | File Templates. */
public class WingSkin extends Skin
{
  private static final NurFont defaultMainFont    = new NurFont(new Font("Times", 0, 0), "Times", "0");
  private static final NurFont defaultTitleFont   = new NurFont(new Font("Times", 0, 0), "Times", "0");
  private static final NurFont defaultDirFont     = new NurFont(new Font("Times", 0, 0), "Times", "-1");
  private static final NurFont defaultCaptionFont = new NurFont(new Font("Times", 0, 0), "Times", "0");

  /** Creates a new WingSkin object. */
  public WingSkin()
  {
    super(TagsAndStuff.BACKGROUND_WING, defaultMainFont, defaultTitleFont, defaultDirFont, defaultCaptionFont);
  }

  /** Creates a new WingSkin object. */
  public WingSkin(String backgroundFileName, NurFont mainFont, NurFont titleFont, NurFont dirFont, NurFont captionFont)
  {
    super(backgroundFileName, mainFont, titleFont, dirFont, captionFont);
  }
}
