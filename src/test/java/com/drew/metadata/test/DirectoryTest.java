/*
 * Created by dnoakes on 29-Nov-2002 08:40:07 using IntelliJ IDEA.
 */
package com.drew.metadata.test;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import org.testng.annotations.Test;
import java.util.GregorianCalendar;
import static java.util.Calendar.JANUARY;
import static org.testng.Assert.assertEquals;

/**  */
@Test(groups = "unit")
public class DirectoryTest
{
  public void testSetAndGetInt() throws Exception
  {
    Metadata  metadata  = new Metadata();
    Directory directory = metadata.getDirectory(DirectoryImpl.class);
    int       value     = 321;
    int       tagType   = 123;

    directory.setInt(tagType, value);
    assertEquals(value, directory.getInt(tagType));
    assertEquals(Integer.toString(value), directory.getString(tagType));
  }

  public void testSetAndGetIntArray() throws Exception
  {
    Metadata  metadata    = new Metadata();
    Directory directory   = metadata.getDirectory(DirectoryImpl.class);
    int[]     inputValues = { 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
    int       tagType     = 123;

    directory.setIntArray(tagType, inputValues);

    int[] outputValues = directory.getIntArray(tagType);

    assertEquals(inputValues.length, outputValues.length);

    for (int i = 0; i < inputValues.length; i++)
    {
      int inputValue  = inputValues[i];
      int outputValue = outputValues[i];

      assertEquals(inputValue, outputValue);
    }

    assertEquals(inputValues, directory.getIntArray(tagType));

    StringBuffer outputString = new StringBuffer();

    for (int i = 0; i < inputValues.length; i++)
    {
      int inputValue = inputValues[i];

      if (i > 0)
      {
        outputString.append(' ');
      }

      outputString.append(inputValue);
    }

    assertEquals(outputString.toString(), directory.getString(tagType));
  }

  public void testSetStringAndGetDate() throws Exception
  {
    Metadata  metadata  = new Metadata();
    Directory directory = metadata.getDirectory(DirectoryImpl.class);
    String    date1     = "2002:01:30 24:59:59";
    String    date2     = "2002:01:30 24:59";
    String    date3     = "2002-01-30 24:59:59";
    String    date4     = "2002-01-30 24:59";

    directory.setString(1, date1);
    directory.setString(2, date2);
    directory.setString(3, date3);
    directory.setString(4, date4);
    assertEquals(date1, directory.getString(1));
    assertEquals(new GregorianCalendar(2002, JANUARY, 30, 24, 59, 59).getTime(), directory.getDate(1));
    assertEquals(new GregorianCalendar(2002, JANUARY, 30, 24, 59, 0).getTime(), directory.getDate(2));
    assertEquals(new GregorianCalendar(2002, JANUARY, 30, 24, 59, 59).getTime(), directory.getDate(3));
    assertEquals(new GregorianCalendar(2002, JANUARY, 30, 24, 59, 0).getTime(), directory.getDate(4));
  }

  public void testSetIntArrayGetByteArray() throws Exception
  {
    Metadata  metadata  = new Metadata();
    Directory directory = metadata.getDirectory(DirectoryImpl.class);
    int[]     ints      = { 1, 2, 3, 4, 5 };

    directory.setIntArray(1, ints);
    assertEquals(ints.length, directory.getByteArray(1).length);
    assertEquals(1, directory.getByteArray(1)[0]);
  }

  public void testSetStringGetInt() throws Exception
  {
    Metadata  metadata  = new Metadata();
    Directory directory = metadata.getDirectory(DirectoryImpl.class);
    byte[]    bytes     = { 0x01, 0x01 };

    directory.setString(1, new String(bytes));
    assertEquals(0x0101, directory.getInt(1));
  }
}
