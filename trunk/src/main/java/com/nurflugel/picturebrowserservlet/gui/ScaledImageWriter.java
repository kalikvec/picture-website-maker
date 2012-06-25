package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.util.UtilMethods;
import org.apache.log4j.Category;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.swing.*;
import static com.nurflugel.picturebrowserservlet.util.UtilMethods.readImage;

public class ScaledImageWriter extends ImageScaler
{
  private static final Category logger = LogFactory.getInstance(ScaledImageWriter.class);
  private static final int      WIDTH  = 768;
  private static final int      HEIGHT = 1024;
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
        logger.debug("" + thumbnailFile.getName() + " for " + file + "time= " + getFormattedDateTime());
      }

      BufferedImage bufferedImage = readImage(thumbnailFile);

      smallerIcon = new ImageIcon(bufferedImage);
    }
    else                         // create it
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("ThumbnailReaderWriter.writeThumbnailImage");
      }

      createSmallerImage(file, thumbnailFile, WIDTH, HEIGHT);
      smallerIcon = getOrCreateImageFromFile(thumbnailFile, file);
    }

    return smallerIcon;
  }
}
