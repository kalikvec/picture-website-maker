package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import java.io.File;

/**  */
public class Gif extends GraphicFile
{
  public Gif(File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
  }
}
