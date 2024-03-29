/*
 * MetadataTest.java
 *
 * Test class written by Drew Noakes.
 *
 * This is public domain software - that is, you can do whatever you want
 * with it, and include it software that is licensed under the GNU or the
 * BSD license, or whatever other licence you choose, including proprietary
 * closed source licenses.  I do ask that you leave this header in tact.
 *
 * If you make modifications to this code that you think would benefit the
 * wider community, please send me a copy and I'll post it on my site.
 *
 * If you make use of this code, I'd appreciate hearing about it.
 *   drew@drewnoakes.com
 * Latest version of this software kept at
 *   http://drewnoakes.com/
 *
 * Created by dnoakes on 26-Oct-2002 18:35:12 using IntelliJ IDEA.
 */
package com.drew.metadata.test;

import com.drew.lang.NullOutputStream;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.iptc.IptcDirectory;
import org.testng.annotations.Test;
import java.io.*;
import java.util.Iterator;
import static com.drew.imaging.jpeg.JpegMetadataReader.readMetadata;
import static com.drew.metadata.exif.ExifDirectory.TAG_APERTURE;
import static com.drew.metadata.exif.ExifDirectory.TAG_BATTERY_LEVEL;
import static com.drew.metadata.exif.GpsDirectory.TAG_GPS_ALTITUDE;
import static com.drew.metadata.exif.GpsDirectory.TAG_GPS_DEST_BEARING;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**
 * JUnit test case for class Metadata.
 *
 * @author  Drew Noakes http://drewnoakes.com
 */
@Test(groups = "unit")
public class MetadataTest
{
  public void testSetAndGetSingleTag() throws Exception
  {
    Metadata  metadata  = new Metadata();
    Directory directory = metadata.getDirectory(ExifDirectory.class);

    directory.setInt(TAG_APERTURE, 1);
    assertEquals(1, directory.getInt(TAG_APERTURE));
  }

  public void testSetSameTagMultpleTimes() throws Exception
  {
    Metadata  metadata  = new Metadata();
    Directory directory = metadata.getDirectory(ExifDirectory.class);

    directory.setInt(TAG_APERTURE, 1);
    directory.setInt(TAG_APERTURE, 2);
    assertEquals(2, directory.getInt(TAG_APERTURE), "setting the tag with a different value should override old value");
  }

  public void testGetDirectory() throws Exception
  {
    Metadata metadata = new Metadata();

    assertTrue(metadata.getDirectory(ExifDirectory.class) instanceof ExifDirectory);
  }

  public void testSetAndGetMultipleTagsInSingleDirectory() throws Exception
  {
    Metadata  metadata = new Metadata();
    Directory exifDir  = metadata.getDirectory(ExifDirectory.class);

    exifDir.setString(TAG_APERTURE, "Tag Value");
    exifDir.setString(TAG_BATTERY_LEVEL, "Another tag");
    assertEquals("Tag Value", exifDir.getString(TAG_APERTURE));
    assertEquals("Another tag", exifDir.getString(TAG_BATTERY_LEVEL));
  }

  public void testSetAndGetMultipleTagsInMultilpeDirectories() throws Exception
  {
    Metadata  metadata = new Metadata();
    Directory exifDir  = metadata.getDirectory(ExifDirectory.class);
    Directory gpsDir   = metadata.getDirectory(GpsDirectory.class);

    exifDir.setString(TAG_APERTURE, "ExifAperture");
    exifDir.setString(TAG_BATTERY_LEVEL, "ExifBatteryLevel");
    gpsDir.setString(TAG_GPS_ALTITUDE, "GpsAltitude");
    gpsDir.setString(TAG_GPS_DEST_BEARING, "GpsDestBearing");
    assertEquals("ExifAperture", exifDir.getString(TAG_APERTURE));
    assertEquals("ExifBatteryLevel", exifDir.getString(TAG_BATTERY_LEVEL));
    assertEquals("GpsAltitude", gpsDir.getString(TAG_GPS_ALTITUDE));
    assertEquals("GpsDestBearing", gpsDir.getString(TAG_GPS_DEST_BEARING));
  }

  /*
   *  public void testCountTags() throws Exception
   *  {
   *      Metadata info = new Metadata();
   *      assertEquals(0, info.countTags());
   *
   *      info.setString(ExifReader.DIRECTORY_EXIF_EXIF, ExifDirectory.TAG_APERTURE, "ExifAperture");
   *      assertEquals(1, info.countTags());
   *      info.setString(ExifReader.DIRECTORY_EXIF_EXIF, ExifDirectory.TAG_BATTERY_LEVEL, "ExifBatteryLevel");
   *      assertEquals(2, info.countTags());
   *      info.setString(ExifReader.DIRECTORY_EXIF_GPS, GpsDirectory.TAG_GPS_ALTITUDE, "GpsAltitude");
   *      assertEquals(3, info.countTags());
   *      info.setString(ExifReader.DIRECTORY_EXIF_GPS, GpsDirectory.TAG_GPS_DEST_BEARING, "GpsDestBearing");
   *      assertEquals(4, info.countTags());
   *  }
   */
  public void testContainsTag() throws Exception
  {
    Metadata  metadata = new Metadata();
    Directory exifDir  = metadata.getDirectory(ExifDirectory.class);

    assertTrue(!exifDir.containsTag(TAG_APERTURE));
    exifDir.setString(TAG_APERTURE, "Tag Value");
    assertTrue(exifDir.containsTag(TAG_APERTURE));
  }

  public void testGetNonExistantTag() throws Exception
  {
    Metadata  metadata = new Metadata();
    Directory exifDir  = metadata.getDirectory(ExifDirectory.class);

    assertEquals(null, exifDir.getString(TAG_APERTURE));
  }

  public void testHasErrors() throws Exception
  {
    Metadata metadata = readMetadata(new File("src/com/drew/metadata/exif/test/badExif.jpg"));

    assertTrue(metadata.getDirectory(ExifDirectory.class).hasErrors(), "exif error");
    metadata = readMetadata(new File("src/com/drew/metadata/exif/test/withExif.jpg"));
    assertTrue(!metadata.getDirectory(ExifDirectory.class).hasErrors(), "no errors");
  }

  public void testGetErrors() throws Exception
  {
    Metadata metadata = readMetadata(new File("src/com/drew/metadata/exif/test/badExif.jpg"));
    Iterator errors   = metadata.getDirectory(ExifDirectory.class).getErrors();

    assertTrue(errors.hasNext());

    String error = (String) errors.next();

    assertEquals("Exif data segment must contain at least 14 bytes", error);
    assertTrue(!errors.hasNext());
  }

  public void testGetErrorCount() throws Exception
  {
    Metadata metadata = readMetadata(new File("src/com/drew/metadata/exif/test/badExif.jpg"));

    assertEquals(1, metadata.getDirectory(ExifDirectory.class).getErrorCount());
  }

  public void testMetadataSerializable() throws Exception
  {
    Metadata metadata = readMetadata(new File("src/com/drew/metadata/test/withIptcExifGps.jpg"));

    new ObjectOutputStream(new NullOutputStream()).writeObject(metadata);
  }

  public void testSerializeAndRestore() throws Exception
  {
    Metadata metadataWrite = readMetadata(new File("src/com/drew/metadata/test/withIptcExifGps.jpg"));
    Metadata metadataRead;
    File     ser           = File.createTempFile("test", "ser");

    try
    {
      // write the ser object
      new ObjectOutputStream(new FileOutputStream(ser)).writeObject(metadataWrite);

      // read the ser object
      metadataRead = (Metadata) new ObjectInputStream(new FileInputStream(ser)).readObject();

      // make sure they're equivalent
      // TODO should compare the two objects via iteration of directories and tags
      assertTrue(metadataRead.containsDirectory(ExifDirectory.class));
      assertTrue(metadataRead.containsDirectory(IptcDirectory.class));
    }
    finally
    {
      ser.delete();
    }
  }
}
