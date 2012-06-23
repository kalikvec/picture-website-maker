package com.nurflugel.picturebrowserservlet.gui;

import java.io.File;

public class SmThumbnailReaderWriter extends ThumbnailReaderWriter
{
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

    if (fileName.indexOf(".jpg") > 0)
    {
      thumbnailName = fileName.replaceAll(".jpg", "_sm.jpg");
    }
    else if (fileName.indexOf(".JPG") > 0)
    {
      thumbnailName = fileName.replaceAll(".JPG", "_sm.jpg");
    }

    return new File(sourceFile.getParentFile(), thumbnailName);
  }
}
