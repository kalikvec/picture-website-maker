package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import org.apache.log4j.Logger;
import java.io.File;

public abstract class ThumbnailReaderWriter extends ImageScaler
{
  private static final Logger logger = LogFactory.getInstance(ThumbnailReaderWriter.class);

  /** Creates a new ThumbnailReaderWriter object. */
  protected ThumbnailReaderWriter() {}
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  public abstract File getThumbnailFile(File sourceFile);

  /**  */
  public abstract String getThumbnailFileName(File file);
}
