package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import com.nurflugel.picturebrowserservlet.gui.TagsAndStuff;
import java.io.File;

public class Mp3 extends SoundFile
{
  private static final long serialVersionUID = 3407160848242301394L;

  public Mp3(File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
    System.out.println("Mp3.Mp3 file=" + file.getName());
  }

  @Override
  protected String getThumbnailName()
  {
    return TagsAndStuff.SOUND_ICON;
  }
}
