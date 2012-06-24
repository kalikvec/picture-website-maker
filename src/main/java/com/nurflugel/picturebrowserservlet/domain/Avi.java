package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import java.io.File;

public class Avi extends MovieFile
{
  private static final long serialVersionUID = -1159597011481439677L;

  public Avi(File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
  }
}
