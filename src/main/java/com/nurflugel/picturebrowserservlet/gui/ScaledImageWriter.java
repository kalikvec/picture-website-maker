package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.UtilMethods;
import org.apache.log4j.Category;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;

public class ScaledImageWriter extends ImageScaler
{
  private static final Category logger = LogFactory.getInstance(ScaledImageWriter.class);
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  @Override
  public ImageIcon getOrCreateImageFromFile(File thumbnailFile, File file) throws IOException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("ThumbnailReaderWriter.getOrCreateImageFromFile");
    }

    ImageIcon smallerIcon;

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

      createSmallerImage(file, thumbnailFile, 768, 1024);
      smallerIcon = getOrCreateImageFromFile(thumbnailFile, file);
    }

    return smallerIcon;
  }
}
