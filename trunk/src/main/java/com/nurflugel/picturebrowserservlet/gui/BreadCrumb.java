package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import org.apache.log4j.Category;

class BreadCrumb extends JLabel implements MouseListener
{
  private File            dir;
  private BreadCrumbPanel breadCrumbPanel;
  private Category        logger = LogFactory.getInstance(BreadCrumb.class);

  BreadCrumb(File dir, String displayText, BreadCrumbPanel breadCrumbPanel)
  {
    this.dir             = dir;
    this.breadCrumbPanel = breadCrumbPanel;
    setText(displayText);
  }

  // ------------------------ INTERFACE METHODS ------------------------
  // --------------------- Interface MouseListener ---------------------
  public void mouseClicked(MouseEvent e)
  {
    breadCrumbPanel.getMainFrame().setCurrentDir(dir, true);
  }

  public void mousePressed(MouseEvent e) {}

  public void mouseReleased(MouseEvent e) {}

  public void mouseEntered(MouseEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Dir will be " + dir.toString());
    }
  }

  public void mouseExited(MouseEvent e) {}

  // ------------------------ GETTER/SETTER METHODS ------------------------
  public File getDir()
  {
    return dir;
  }
}
