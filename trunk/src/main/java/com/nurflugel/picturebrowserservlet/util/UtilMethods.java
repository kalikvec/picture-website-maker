package com.nurflugel.picturebrowserservlet.util;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.gui.GifDecoder;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.log4j.Category;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Collection;

public class UtilMethods
{
  private static Category logger = LogFactory.getInstance(UtilMethods.class);

  /**  */
  public static int getNumRowsFromString(String numRowsPerPageString)
  {
    int numRows = 0;

    if (numRowsPerPageString.equals("1")       //
          || numRowsPerPageString.equals("2")  //
          || numRowsPerPageString.equals("3")  //
          || numRowsPerPageString.equals("4")  //
          || numRowsPerPageString.equals("5")  //
          || numRowsPerPageString.equals("6")  //
          || numRowsPerPageString.equals("7")  //
          || numRowsPerPageString.equals("8")  //
          || numRowsPerPageString.equals("9"))
    {
      numRows = new Integer(numRowsPerPageString);
    }
    else if (numRowsPerPageString.equals("Select"))
    {
      numRows = 4;
    }
    else if (numRowsPerPageString.equals("Unlimited"))
    {
      numRows = 99999999;
    }

    return numRows;
  }

  /**  */
  public static void copyJpg(File sourceFile, File targetFile) throws IOException
  {
    if (sourceFile.exists())
    {
      BufferedImage        bufferedImage = readImage(sourceFile);
      BufferedOutputStream out           = new BufferedOutputStream(new FileOutputStream(targetFile));
      JPEGImageEncoder     encoder       = JPEGCodec.createJPEGEncoder(out);

      encoder.encode(bufferedImage);
    }
  }

  /**  */
  public static BufferedImage readImage(File file) throws IOException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("UtilMethods.readImage");
    }

    BufferedImage bufferedImage     = null;
    String        upperCaseFileName = file.getName().toUpperCase();

    if (upperCaseFileName.endsWith(".JPG"))
    {
      bufferedImage = readJpegImage(file);
    }
    else if (upperCaseFileName.endsWith(".GIF"))
    {
      bufferedImage = readGifImage(file);
    }

    return bufferedImage;
  }

  /**  */
  private static BufferedImage readJpegImage(File file) throws IOException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("UtilMethods.readJpegImage");
    }

    String              filePath        = file.getAbsolutePath();
    FileInputStream     fileInputStream = new FileInputStream(filePath);
    BufferedInputStream in              = new BufferedInputStream(fileInputStream);
    JPEGImageDecoder    jpegDecoder     = JPEGCodec.createJPEGDecoder(in);

    return jpegDecoder.decodeAsBufferedImage();
  }

  /**  */
  private static BufferedImage readGifImage(File file)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("UtilMethods.readGifImage");
    }

    String name;

    if (isSystemFile(file))
    {
      name = "images/" + file.getName();
    }
    else
    {
      name = file.getName();
    }

    InputStream         inputStream         = BufferedImage.class.getClassLoader().getResourceAsStream(name);
    BufferedInputStream bufferedImputStream = new BufferedInputStream(inputStream);
    GifDecoder          gifDecoder          = new GifDecoder();

    return gifDecoder.getBufferedImage(bufferedImputStream);
  }

  /**  */
  public static boolean isSystemFile(File file)
  {
    return isSystemFile(file.getAbsolutePath());
  }

  /**  */
  public static boolean isSystemFile(String fileName)
  {
    String        newFileName       = fileName.toLowerCase();
    boolean       isSmallThumbnail  = newFileName.contains("_sm");
    boolean       isSystemFile      = false;
    String        upperCaseFileName = newFileName.toUpperCase();
    SystemFiles[] values            = SystemFiles.values();

    for (SystemFiles systemFile : values)
    {
      if (upperCaseFileName.contains(systemFile.toString().toUpperCase()))
      {
        isSystemFile = true;

        break;
      }
    }

    return isSmallThumbnail || isSystemFile;
  }

  /**  */
  public static boolean isAnimation(String fileName)
  {
    String fileNameUpperCase = fileName.toUpperCase();

    if (logger.isDebugEnabled())
    {
      logger.debug("UtilMethods.isAnimation - filename is " + fileNameUpperCase);
    }

    boolean isAnimation = false;

    if (fileNameUpperCase.endsWith("MPG")        //
          || fileNameUpperCase.endsWith("MPEG")  //
          || fileNameUpperCase.endsWith("AVI")   //
          || fileNameUpperCase.endsWith("MOV"))
    {
      isAnimation = true;
    }

    if (logger.isDebugEnabled())
    {
      logger.debug("isAnimation = " + isAnimation);
    }

    return isAnimation;
  }

  /**  */
  public static boolean isMp3(String fileName)
  {
    String  fileNameLowerCase = fileName.toLowerCase();
    boolean isMatch           = (fileNameLowerCase.indexOf(".mp3") > 0) || (fileNameLowerCase.indexOf(".wav") > 0);

    if (logger.isDebugEnabled())
    {
      logger.debug("fileNameLowerCase = " + fileNameLowerCase);
      logger.debug("isMatch = " + isMatch);
    }

    return isMatch;
  }

  /** Creates a new UtilMethods object. */
  private UtilMethods() {}

  /**
   * Add a line of text ot the lines.
   *
   * @param  numTabs  the number of tabs to indent
   * @param  text     the text to add
   * @param  lines    the collection of lines
   */
  public static void addToLines(int numTabs, String text, Collection<String> lines)
  {
    StringBuilder builder = new StringBuilder();

    for (int i = 0; i < numTabs; i++)
    {
      builder.append("  ");
    }

    builder.append(text);
    lines.add(builder.toString());
  }
}
