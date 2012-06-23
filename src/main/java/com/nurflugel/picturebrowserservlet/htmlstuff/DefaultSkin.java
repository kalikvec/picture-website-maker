package com.nurflugel.picturebrowserservlet.htmlstuff;

import com.nurflugel.picturebrowserservlet.gui.TagsAndStuff;
import java.awt.*;

public class DefaultSkin extends Skin
{
  private static final NurFont defaultMainFont    = new NurFont(new Font("Times", 0, 0), "Times", "0");
  private static final NurFont defaultTitleFont   = new NurFont(new Font("Times", 0, 0), "Times", "0");
  private static final NurFont defaultDirFont     = new NurFont(new Font("Times", 0, 0), "Times", "-1");
  private static final NurFont defaultCaptionFont = new NurFont(new Font("Times", 0, 0), "Times", "0");

  /** Creates a new DefaultSkin object. */
  public DefaultSkin()
  {
    super(TagsAndStuff.BACKGROUND_DEFAULT, defaultMainFont, defaultTitleFont, defaultDirFont, defaultCaptionFont);
  }

  /** Creates a new DefaultSkin object. */
  public DefaultSkin(String backgroundFileName, NurFont mainFont, NurFont titleFont, NurFont dirFont, NurFont captionFont)
  {
    super(backgroundFileName, mainFont, titleFont, dirFont, captionFont);
  }
}
