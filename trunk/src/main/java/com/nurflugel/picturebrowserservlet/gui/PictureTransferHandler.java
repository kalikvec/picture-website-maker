package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import org.apache.log4j.Category;

/**
 * Handles the drag-n-drop stuff.
 *
 * @deprecated
 */
class PictureTransferHandler extends TransferHandler
{
  DataFlavor       pictureFlavor = DataFlavor.imageFlavor;
  PicLabel         sourcePic;
  boolean          shouldRemove;
  private Category logger        = LogFactory.getInstance(PictureTransferHandler.class);

  // ------------------------ OTHER METHODS ------------------------
  protected void exportDone(JComponent c, Transferable data, int action)
  {
    if (shouldRemove && (action == MOVE))
    {
      // sourcePic.setImage(null);
    }

    sourcePic = null;
  }

  public int getSourceActions(JComponent c)
  {
    return COPY_OR_MOVE;
  }

  /**  */
  public boolean importData(JComponent c, Transferable t)
  {
    if (canImport(c, t.getTransferDataFlavors()))
    {
      PicLabel pic = (PicLabel) c;

      // Don't drop on myself.
      if (sourcePic.equals(pic))
      {
        shouldRemove = false;

        return true;
      }

      try
      {
        Image image = (Image) t.getTransferData(pictureFlavor);

        // Set the component to the new picture.
        // pic.setImage(image);
        return true;
      }
      catch (UnsupportedFlavorException ufe)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("importData: unsupported data flavor");
        }
      }
      catch (IOException ioe)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("importData: I/O exception");
        }
      }
    }

    return false;
  }

  /**  */
  public boolean canImport(JComponent c, DataFlavor[] flavors)
  {
    for (DataFlavor flavor : flavors)
    {
      if (pictureFlavor.equals(flavor))
      {
        return true;
      }
    }

    return false;
  }
}
