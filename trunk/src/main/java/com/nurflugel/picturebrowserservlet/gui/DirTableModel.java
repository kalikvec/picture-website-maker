package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import org.apache.log4j.Category;
import java.util.List;
import javax.swing.table.DefaultTableModel;

/**  */
public class DirTableModel extends DefaultTableModel
{
  private static final Category logger           = LogFactory.getInstance(DirTableModel.class);
  private boolean[]             canEdit          = new boolean[] { false, true };
  private static final long     serialVersionUID = 5269910075261623661L;

  /** Creates a new DirTableModel object. */
  public DirTableModel(List<Dir> dirList)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("DirTableModel.DirTableModel");
    }

    Object[][] data        = new Object[dirList.size()][2];
    int        dirListSize = dirList.size();

    for (int i = 0; i < dirListSize; i++)
    {
      Dir dir = dirList.get(i);

      data[i][0] = dir;
      data[i][1] = dir.getDescription();
    }

    String[] columnTitles = { "Directory", "Description" };

    setDataVector(data, columnTitles);
  }

  // ------------------------ OTHER METHODS ------------------------
  @Override
  @SuppressWarnings("RefusedBequest")
  public boolean isCellEditable(int row, int column)
  {
    return getCanEdit()[column];
  }
  // ------------------------ GETTER/SETTER METHODS ------------------------

  /**  */
  public boolean[] getCanEdit()
  {
    return canEdit;
  }

  /**  */
  public void setCanEdit(boolean[] canEdit)
  {
    this.canEdit = canEdit;
  }
}
