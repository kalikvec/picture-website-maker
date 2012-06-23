package com.nurflugel.picturebrowserservlet.gui;

public class ThumbnailReaderWriterFactory
{
  /** Creates a new ThumbnailReaderWriterFactory object. */
  private ThumbnailReaderWriterFactory() {}

  /**  */
  public static ThumbnailReaderWriter getThumbnailReaderWriter(boolean isDirButtonselected)
  {
    ThumbnailReaderWriter thumbnailReaderWriter;

    if (isDirButtonselected)
    {
      thumbnailReaderWriter = new DirThumbnailReaderWriter();
    }
    else
    {
      thumbnailReaderWriter = new SmThumbnailReaderWriter();
    }

    return thumbnailReaderWriter;
  }
}
