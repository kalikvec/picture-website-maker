package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import org.apache.log4j.Category;
import java.io.File;

public class DirThumbnailReaderWriter extends ThumbnailReaderWriter
{
  private Category logger = LogFactory.getInstance(DirThumbnailReaderWriter.class);

  // ------------------------ OTHER METHODS ------------------------
  @Override
  public String getThumbnailFileName(File sourceFile)
  {
    File thumbnailFile = getThumbnailFile(sourceFile);
    File thumbFileDir  = thumbnailFile.getParentFile();

    return thumbFileDir.getName() + "/" + thumbnailFile.getName();
  }

  /**  */
  @Override
  public File getThumbnailFile(File sourceFile)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("");
    }

    String fileName     = sourceFile.getName();
    File   parentFile   = sourceFile.getParentFile();
    File   thumbnailDir = new File(parentFile, "thumbnails");

    if (!thumbnailDir.exists())
    {
      thumbnailDir.mkdir();
    }

    return new File(thumbnailDir, fileName);
  }
}
