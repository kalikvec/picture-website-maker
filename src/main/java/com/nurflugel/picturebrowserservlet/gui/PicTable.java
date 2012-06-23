package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import org.apache.log4j.Category;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableColumnModel;
import java.awt.*;
import java.util.List;

class PicTable extends JTable implements PicContainer
{
  /** Use serialVersionUID for interoperability. */
  private static final long serialVersionUID = -1229235993359846954L;
  private MainFrame         theFrame;
  private Category          logger           = LogFactory.getInstance(PicTable.class);
  private boolean           showExfDropdowns;
  private int               numColumns;

  /** Creates a new PicTable object. */
  PicTable(MainFrame theFrame, List<MediaFile> pics)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicTable.PicTable");
    }

    this.theFrame = theFrame;
    setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    populateTable(pics);
  }

  PicTable(MainFrame mainFrame, List<MediaFile> pics, int numColumns, Dir dirpage)
  {
    this.numColumns = numColumns;
    setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
    populateTable(pics);
  }

  /**  */
  private void populateTable(List<MediaFile> pics)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicTable.populateTable");
    }

    if (numColumns == 0)
    {
      numColumns = 5;
    }

    DefaultTableColumnModel columnModel   = new DefaultTableColumnModel();
    PicTableModel           picTableModel = new PicTableModel(pics, theFrame, numColumns);

    setColumnModel(columnModel);
    setModel(picTableModel);
    setBackground((Color) UIManager.getDefaults().get("Button.background"));
    setBorder(new SoftBevelBorder(BevelBorder.RAISED));
    setModel(picTableModel);
    setRowHeights();
    createPreviews();
  }

  /**
   * Create the "preview" pics - 1024 x 768, if they dont' already exist. Dont do this if the pics are 1024x768 or less resolution, or if they dont'
   * already exist.
   */
  private void createPreviews() {}

  /**  */
  private void setRowHeights()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicTable.setRowHeights");
    }

    int rowCount = getRowCount();

    for (int i = 0; i < rowCount; i++)
    {
      Object   valueAt     = getModel().getValueAt(i, 0);
      PicLabel componentAt = (PicLabel) valueAt;

      if (componentAt != null)
      {
        double height = componentAt.getPreferredSize().getHeight();

        if (logger.isDebugEnabled())
        {
          logger.debug("row, height = " + i + " " + height);
        }

        height = (height < 10) ? 10
                               : height;
        setRowHeight(i, (int) height);
      }
    }
  }

  // ------------------------ OTHER METHODS ------------------------
  /*
   *  JTable uses this method to determine the default renderer editor for each cell.  If we didn't implement this method, then the last column
   * would contain text ("true"/"false"), rather than a check box.
   */
  @Override
  public Class getColumnClass(int c)
  {
    return getValueAt(0, c).getClass();
  }

  /**  */
  public void refresh(List<MediaFile> pics, int numColumns, Dir dirpage)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicTable.refresh");
    }

    this.numColumns = numColumns;

    PicTableModel picTableModel = new PicTableModel(pics, theFrame, numColumns);

    setModel(picTableModel);
    setRowHeights();
    invalidate();
  }

  public void showExifDropdowns(boolean selected)
  {
    showExfDropdowns = selected;
  }

  public void cutAction() {}

  public void deleteAction() {}

  public void pasteAction() {}

  public void renameAction() {}
}
