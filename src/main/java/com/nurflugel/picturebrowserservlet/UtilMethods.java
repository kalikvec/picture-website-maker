package com.nurflugel.picturebrowserservlet;

import com.nurflugel.picturebrowserservlet.gui.GifDecoder;
import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.*;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageDecoder;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Category;
import java.awt.image.BufferedImage;
import java.io.*;

/** Created by IntelliJ IDEA. User: Douglas Bullard Date: May 1, 2003 Time: 10:59:57 PM To change this template use Options | File Templates. */
public class UtilMethods
{
  // todo make enums
  public static String[] SYSTEM_FILES =
  { UP_ARROW, LEFT_ARROW, RIGHT_ARROW, SOUND_ICON_GIF, SOUND_ICON_JPG, VIDEO_ICON_GIF, VIDEO_ICON_JPG, BACKGROUND_DEFAULT, BACKGROUND_WING };
  public static String[] FILES_TO_COPY = { UP_ARROW, LEFT_ARROW, RIGHT_ARROW, BACKGROUND_DEFAULT, SOUND_ICON, VIDEO_ICON };
  private static Category logger       = LogFactory.getInstance(UtilMethods.class);

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
    String  newFileName       = fileName.toLowerCase();
    boolean isSmallThumbnail  = newFileName.contains("_sm");
    boolean isSystemFile      = false;
    String  upperCaseFileName = newFileName.toUpperCase();

    for (String aSYSTEM_FILES : SYSTEM_FILES)
    {
      if (upperCaseFileName.contains(aSYSTEM_FILES.toUpperCase()))
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

  /**  */
  public static void copyFile(InputStream inputStream, File outputFile) throws IOException
  {
    // FileUtils.copyFile(inputStream,outputFile);
    if (logger.isDebugEnabled())
    {
      logger.debug("UtilMethods.copyFile");
    }

    FileOutputStream out = null;

    try
    {
      out = new FileOutputStream(outputFile);

      int    c;
      byte[] b = new byte[1024];

      while ((c = inputStream.read(b)) != -1)
      {
        out.write(b, 0, c);
      }
    }
    finally
    {
      try
      {
        if (inputStream != null)
        {
          inputStream.close();
        }
      }
      finally
      {
        if (out != null)
        {
          out.close();
        }
      }
    }
  }

  /**  */
  public static void copyFile(File inputFile, File outputFile)
  {
    FileInputStream in = null;

    try
    {
      in = new FileInputStream(inputFile);
      copyFile(in, outputFile);
    }
    catch (Exception e)
    {
      logger.error("e = ", e);
    }
    finally
    {
      try
      {
        in.close();
      }
      catch (IOException e)
      {
        logger.error("e = ", e);
      }
    }
  }

  /** Creates a new UtilMethods object. */
  private UtilMethods() {}
}
