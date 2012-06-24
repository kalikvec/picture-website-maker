package com.nurflugel.picturebrowserservlet.gui;

public class ThumbnailReaderWriterFactory
{
  /** Creates a new ThumbnailReaderWriterFactory object. */
  private ThumbnailReaderWriterFactory() {}

  /**  */
  public static ThumbnailReaderWriter getThumbnailReaderWriter(boolean isDirButtonselected)
  {
    ThumbnailReaderWriter thumbnailReaderWriter = isDirButtonselected ? new DirThumbnailReaderWriter()
                                                                      : new SmThumbnailReaderWriter();

    return thumbnailReaderWriter;
  }
}
