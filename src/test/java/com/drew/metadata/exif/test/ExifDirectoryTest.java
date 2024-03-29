/*
 * Created by dnoakes on 25-Nov-2002 20:47:31 using IntelliJ IDEA.
 */
package com.drew.metadata.exif.test;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.imaging.jpeg.JpegSegmentReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
import org.testng.annotations.Test;
import java.io.File;
import static com.drew.metadata.exif.ExifDirectory.TAG_THUMBNAIL_DATA;
import static org.testng.Assert.*;

/**  */
@Test(groups = "unit")
public class ExifDirectoryTest
{
  public void testGetDirectoryName() throws Exception
  {
    Metadata  metadata  = new Metadata();
    Directory directory = metadata.getDirectory(ExifDirectory.class);

    assertEquals("Exif", directory.getName());
  }

  public void testGetThumbnailData() throws Exception
  {
    File          file          = new File("src/com/drew/metadata/exif/test/withExif.jpg");
    Metadata      metadata      = JpegMetadataReader.readMetadata(file);
    ExifDirectory exifDirectory = (ExifDirectory) metadata.getDirectory(ExifDirectory.class);

    assertTrue(exifDirectory.containsTag(TAG_THUMBNAIL_DATA));

    byte[] thumbData = exifDirectory.getThumbnailData();

    try
    {
      new JpegSegmentReader(thumbData);
    }
    catch (JpegProcessingException e)
    {
      fail("Unable to construct JpegSegmentReader from thumbnail data");
    }
  }
}
