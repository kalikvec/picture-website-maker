/*
 * Created by IntelliJ IDEA. User: douglasbulla Date: Dec 29, 2004 Time: 10:21:33 AM
 */
package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.UtilMethods;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;
import org.apache.log4j.Category;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

/**  */
public class ImageScaler
{
  private static final double    IMAGE_SCALE_THRESHOLD = .0001;
  private static final Category  logger                = LogFactory.getInstance(ImageScaler.class);
  private final SimpleDateFormat dateFormat            = new SimpleDateFormat("hh:mm:ss:SS");
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  ImageIcon getOrCreateImageFromFile(File thumbnailFile, File file) throws IOException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("ThumbnailReaderWriter.getOrCreateImageFromFile");
    }

    ImageIcon smallerIcon = null;

    if (thumbnailFile.exists())  // read in thumbnail image directly
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("##################### reading existing thumbnail " + thumbnailFile.getName() + " for " + file + "time= "
                       + getFormattedDateTime());
      }

      BufferedImage bufferedImage = UtilMethods.readImage(thumbnailFile);

      smallerIcon = new ImageIcon(bufferedImage);
    }
    else                         // create it
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("ThumbnailReaderWriter.writeThumbnailImage");
        logger.debug("########### creating new thumbnail!");
      }

      createSmallerImage(file, thumbnailFile, 100, 100);
      smallerIcon = getOrCreateImageFromFile(thumbnailFile, file);
    }

    return smallerIcon;
  }

  /**  */
  String getFormattedDateTime()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("ThumbnailReaderWriter.getFormattedDateTime");
    }

    Date date = new Date();

    return dateFormat.format(date);
  }

  /**  */
  protected BufferedImage createSmallerImage(File file, File thumbnailFile, int width, int height) throws IOException
  {
    BufferedImage smallerImage = makeImageSmaller(file, height, width);

    writeImage(thumbnailFile, smallerImage);

    return smallerImage;
  }

  /**  */
  protected BufferedImage makeImageSmaller(File file, int height, int weight) throws IOException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("ThumbnailReaderWriter.makeImageSmaller " + file.getName());
    }

    BufferedImage bufferedImage = UtilMethods.readImage(file);
    ImageIcon     smallerIcon   = new ImageIcon(bufferedImage);
    int           iconWidth     = smallerIcon.getIconWidth();
    int           iconHeight    = smallerIcon.getIconHeight();

    if (logger.isDebugEnabled())
    {
      logger.debug("Making thumbnail for " + file + "time= " + getFormattedDateTime());
    }

    double        scale;
    BufferedImage smallerImage;

    if (iconWidth > iconHeight)
    {
      int newHeight = (height * iconHeight) / iconWidth;

      newHeight = (newHeight < 10) ? 10
                                   : newHeight;
      scale        = (double) newHeight / iconHeight;
      smallerImage = getScaledImage(scale, bufferedImage);
    }
    else
    {
      int newWidth = (weight * iconWidth) / iconHeight;

      newWidth = (newWidth < 10) ? 10
                                 : newWidth;
      scale        = (double) newWidth / iconWidth;
      smallerImage = getScaledImage(scale, bufferedImage);
    }

    if (logger.isDebugEnabled())
    {
      logger.debug("Making smaller icon for " + file + "time= " + getFormattedDateTime());
    }

    smallerIcon = new ImageIcon(smallerImage);
    iconWidth   = smallerIcon.getIconWidth();
    iconHeight  = smallerIcon.getIconHeight();

    return smallerImage;
  }

  /**  */
  private static BufferedImage getScaledImage(double scale, BufferedImage srcImg)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("ThumbnailReaderWriter.getScaledImage");
    }

    double value = 1 - scale;

    if (value < IMAGE_SCALE_THRESHOLD)
    {
      return srcImg;
    }

    AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(scale, scale), null);

    return op.filter(srcImg, null);
  }

  /**  */
  private static void writeImage(File thumbnailFile, BufferedImage smallerImage) throws IOException
  {
    // save thumbnail image to OUTFILE
    BufferedOutputStream out     = new BufferedOutputStream(new FileOutputStream(thumbnailFile));
    JPEGImageEncoder     encoder = JPEGCodec.createJPEGEncoder(out);
    JPEGEncodeParam      param   = encoder.getDefaultJPEGEncodeParam(smallerImage);
    int                  quality = 100;

    param.setQuality(quality / 100.0f, false);
    encoder.setJPEGEncodeParam(param);
    encoder.encode(smallerImage);
  }
}
