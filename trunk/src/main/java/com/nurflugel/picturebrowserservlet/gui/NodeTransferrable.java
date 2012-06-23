package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import org.apache.log4j.Category;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;

public class NodeTransferrable implements Transferable
{
  public static DataFlavor    INFO_FLAVOR = new DataFlavor(PicLabel.class, "NodeTransferable");
  private static DataFlavor[] flavors     = { INFO_FLAVOR };
  private PicLabel            theLabel;
  private Category            logger      = LogFactory.getInstance(NodeTransferrable.class);

  /** Constructor NodeTransferable. */
  public NodeTransferrable(PicLabel theLabel)
  {
    this.theLabel = theLabel;

    if (logger.isDebugEnabled())
    {
      logger.debug(theLabel.toString());
    }
  }

  // ------------------------ INTERFACE METHODS ------------------------
  // --------------------- Interface Transferable ---------------------
  /**  */
  public DataFlavor[] getTransferDataFlavors()
  {
    return flavors;
  }

  /** Transferable flavor for <code>NodeTransferable</code> . */
  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return flavor.equals(INFO_FLAVOR);
  }

  /**  */
  public Object getTransferData(DataFlavor flavor)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("");
    }

    return theLabel;
  }

  public static DataFlavor[] getFlavors()
  {
    return flavors;
  }

  public static void setFlavors(DataFlavor[] flavors)
  {
    NodeTransferrable.flavors = flavors;
  }

  public PicLabel getTheLabel()
  {
    return theLabel;
  }

  public void setTheLabel(PicLabel theLabel)
  {
    this.theLabel = theLabel;
  }
}
