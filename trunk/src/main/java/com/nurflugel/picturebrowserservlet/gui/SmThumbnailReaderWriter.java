package com.nurflugel.picturebrowserservlet.gui;

import java.io.File;

public class SmThumbnailReaderWriter extends ThumbnailReaderWriter
{
  private static final String JPG    = ".jpg";
  private static final String SM_JPG = "_sm.jpg";
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  @Override
  public String getThumbnailFileName(File file)
  {
    File thumbnailFile = getThumbnailFile(file);

    return thumbnailFile.getName();
  }

  /**  */
  @Override
  public File getThumbnailFile(File sourceFile)
  {
    String thumbnailName = null;
    String fileName      = sourceFile.getName();

    if (fileName.indexOf(JPG) > 0)
    {
      thumbnailName = fileName.replaceAll(JPG, SM_JPG);
    }
    else if (fileName.indexOf(".JPG") > 0)
    {
      thumbnailName = fileName.replaceAll(".JPG", SM_JPG);
    }

    return new File(sourceFile.getParentFile(), thumbnailName);
  }
}
