package com.nurflugel.picturebrowserservlet.gui;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.Serializable;

public class FileListTransferable implements Transferable, Serializable
{
  private String            fileName;
  private static final long serialVersionUID = 1356084047483642348L;

  public FileListTransferable(String fileName)
  {
    this.fileName = fileName;
  }

  public String getFileName()
  {
    return fileName;
  }

  public synchronized Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException
  {
    if (flavor.equals(DataFlavor.stringFlavor))
    {
      return this;
    }
    else
    {
      throw new UnsupportedFlavorException(flavor);
    }
  }

  public DataFlavor[] getTransferDataFlavors()
  {
    return new DataFlavor[] { DataFlavor.stringFlavor };
  }

  public boolean isDataFlavorSupported(DataFlavor flavor)
  {
    return flavor.equals(DataFlavor.stringFlavor);
  }

  public void setFileName(String fileName)
  {
    this.fileName = fileName;
  }
}
