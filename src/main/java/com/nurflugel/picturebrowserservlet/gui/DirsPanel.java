package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import org.apache.log4j.Category;
import javax.swing.*;
import java.awt.*;
import java.util.List;

class DirsPanel extends JPanel
{
  /** Use serialVersionUID for interoperability. */
  private static final long serialVersionUID = -8536803039096808064L;
  private MainFrame         theFrame;
  private Category          logger           = LogFactory.getInstance(DirsPanel.class);

  /** Creates a new DirsPanel object. */
  DirsPanel(MainFrame theFrame, List<Dir> dirs)
  {
    this.theFrame = theFrame;

    if (logger.isDebugEnabled())
    {
      logger.debug("DirsPanel.DirsPanel");
    }

    LayoutManager layoutManager = new FlowLayout();  //

    setLayout(layoutManager);
    populateTable(dirs);
  }

  /**  */
  private void populateTable(List<Dir> dirs)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("DirTable.populateTable");
    }

    for (Dir dir : dirs)
    {
      DirPanel dirPanel = new DirPanel(theFrame, dir);

      add(dirPanel);
    }
  }

  // ------------------------ OTHER METHODS ------------------------
  public void refresh(List<Dir> dirList)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("DirTable.refresh");
    }

    removeAll();
    populateTable(dirList);
    invalidate();
  }
}
