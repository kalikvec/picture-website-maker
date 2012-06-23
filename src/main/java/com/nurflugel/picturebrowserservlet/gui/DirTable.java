package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import org.apache.log4j.Category;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableColumnModel;
import static javax.swing.border.BevelBorder.RAISED;

class DirTable extends JPanel
{
  /** Use serialVersionUID for interoperability. */
  private static final long serialVersionUID     = 4018284468601520571L;
  private static int        MAX_DIR_TABLE_HEIGHT = 300;
  private static int        HEIGHT_PER_ROW       = 25;
  private static double     MULTIPLIER           = 0.95;
  private MainFrame         theFrame;
  private DirTableModel     dirTableModel;
  private JTable            theTable;
  private Category          logger               = LogFactory.getInstance(DirTable.class);

  /** Creates a new DirTable object. */
  DirTable(MainFrame theFrame, List<Dir> dirs)
  {
    theTable = new JTable();

    JScrollPane theScrollPane = new JScrollPane();

    this.theFrame = theFrame;
    theScrollPane.setViewportView(theTable);
    add(theScrollPane);
    setBorder(new EtchedBorder());

    if (logger.isDebugEnabled())
    {
      logger.debug("DirTable.DirTable");
    }

    setDirTableHeight(dirs);
    populateTable(dirs);
  }

  /**  */
  private void setDirTableHeight(List<Dir> dirs)
  {
    int panelWidth = getWidth();
    int frameWidth = theFrame.getWidth();

    panelWidth = (panelWidth < 100) ? frameWidth
                                    : panelWidth;

    int width  = panelWidth;
    int height = dirs.size() * theTable.getRowHeight();

    height = (height < MAX_DIR_TABLE_HEIGHT) ? height
                                             : MAX_DIR_TABLE_HEIGHT;

    if (logger.isDebugEnabled())
    {
      logger.debug("width = " + width);
    }

    theTable.setPreferredScrollableViewportSize(new Dimension(width, height));
  }

  /**  */
  private void populateTable(List<Dir> dirs)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("DirTable.populateTable");
    }

    DefaultTableColumnModel columnModel = new DefaultTableColumnModel();

    dirTableModel = new DirTableModel(dirs);
    theTable.setColumnModel(columnModel);
    theTable.setModel(dirTableModel);
    setBackground((Color) UIManager.getDefaults().get("Button.background"));
    setBorder(new SoftBevelBorder(RAISED));
    theTable.setModel(dirTableModel);
    theTable.addMouseListener(new MouseAdapter()
      {
        @Override
        public void mouseClicked(MouseEvent evt)
        {
          mouseClickedAction();
        }
      });
  }

  /**  */
  private void mouseClickedAction()
  {
    int selectedRow    = theTable.getSelectedRow();
    int selectedColumn = theTable.getSelectedColumn();

    if (logger.isDebugEnabled())
    {
      logger.debug("selectedRow = " + selectedRow);
      logger.debug("selectedColumn = " + selectedColumn);
    }

    switch (selectedColumn)
    {
      case 0:

        Dir  selectedDir = (Dir) dirTableModel.getValueAt(selectedRow, 0);
        File newDir      = selectedDir.getFile();

        theFrame.setCurrentDir(newDir, true);
        break;

      default:
    }
  }

  // ------------------------ OTHER METHODS ------------------------
  public String getDescriptionAt(int selectedRow)
  {
    return (String) dirTableModel.getValueAt(selectedRow, 1);
  }

  /**  */
  public Dir getDirAt(int selectedRow)
  {
    Object valueAt = dirTableModel.getValueAt(selectedRow, 0);

    return (Dir) valueAt;
  }

  /**  */
  public void refresh(List<Dir> dirList)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("DirTable.refresh");
    }

    dirTableModel = new DirTableModel(dirList);
    theTable.setModel(dirTableModel);
    setDirTableHeight(dirList);
    invalidate();
  }

  /**  */
  public int getRowCount()
  {
    return theTable.getRowCount();
  }
}
