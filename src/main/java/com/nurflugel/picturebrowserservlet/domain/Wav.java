package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import java.io.File;

public class Wav extends SoundFile
{
  private static final long serialVersionUID = 8968153828264084913L;

  public Wav(File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
  }
}
