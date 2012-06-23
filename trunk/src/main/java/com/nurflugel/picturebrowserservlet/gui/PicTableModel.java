package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import org.apache.log4j.Category;
import javax.swing.table.DefaultTableModel;
import java.util.List;

class PicTableModel extends DefaultTableModel
{
  /** Use serialVersionUID for interoperability. */
  private static final long serialVersionUID = 3497376056777762139L;
  private boolean[]         canEdit          = new boolean[] { false, true };
  private Category          logger           = LogFactory.getInstance(PicTableModel.class);

  /** Creates a new PicTableModel object. */
  PicTableModel(List<MediaFile> pics, MainFrame mainFrame, int numColumns)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicTableModel.PicTableModel");
      // ProgressMonitor progressMonitor = new ProgressMonitor(null, "Making thumbnails...", "", 0, pics.size()); progressMonitor.setProgress(0);
      // progressMonitor.setMillisToDecideToPopup(1 / 2);
    }

    Object[][] data = new Object[pics.size()][numColumns];

    for (int i = 0; i < pics.size(); i++)
    {
      MediaFile pic = pics.get(i);

      data[i][0] = new PicLabel(pic, mainFrame, null);

      // progressMonitor.setProgress(i);
    }

    String[] columnTitles = { "", "" };

    setDataVector(data, columnTitles);
  }

  // ------------------------ OTHER METHODS ------------------------
  @Override
  public boolean isCellEditable(int row, int column)
  {
    return canEdit[column];
  }
}
