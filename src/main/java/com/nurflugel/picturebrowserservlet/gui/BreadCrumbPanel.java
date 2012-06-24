package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.settings.Settings;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.StringTokenizer;
import static java.io.File.separator;
import static javax.swing.JFileChooser.APPROVE_OPTION;
import static javax.swing.JFileChooser.DIRECTORIES_ONLY;

class BreadCrumbPanel extends JPanel implements ActionListener
{
  /** Use serialVersionUID for interoperability. */
  private static final long serialVersionUID = 6782999730866390853L;
  private MainFrame         theFrame;
  private JButton           browseButton;

  /** Creates a new BreadCrumbPanel object. */
  BreadCrumbPanel(MainFrame frame)
  {
    theFrame = frame;
    setLayout(new FlowLayout());
    browseButton = new JButton("Browse");
    browseButton.addActionListener(this);
  }

  // ------------------------ INTERFACE METHODS ------------------------
  // --------------------- Interface ActionListener ---------------------
  /** Invoked when an action occurs. */
  public void actionPerformed(ActionEvent e)
  {
    JFileChooser fileChooser    = new JFileChooser();
    Settings     settings       = theFrame.getSettings();
    String       lastVisitedDir = settings.getLastVisitedDir();

    if (lastVisitedDir != null)
    {
      fileChooser.setCurrentDirectory(new File(lastVisitedDir));
    }

    fileChooser.setFileSelectionMode(DIRECTORIES_ONLY);

    int returnVal = fileChooser.showOpenDialog(theFrame);

    if (returnVal == APPROVE_OPTION)
    {
      File selectedFile = fileChooser.getSelectedFile();

      if ((selectedFile != null) && selectedFile.isDirectory())
      {
        theFrame.setCurrentDir(selectedFile, true);
      }
    }
  }

  // ------------------------ OTHER METHODS ------------------------
  /**  */
  public MainFrame getMainFrame()
  {
    return theFrame;
  }

  /**  */
  void populateBreadCrumbPanel()
  {
    add(browseButton);
    invalidate();
  }

  /**  */
  void populateBreadCrumbPanel(File theCurrentDir)
  {
    removeAll();

    String          currentPath = theCurrentDir.getAbsolutePath();
    StringTokenizer tokenizer   = new StringTokenizer(currentPath, "\\");
    StringBuilder   thePath     = new StringBuilder();

    while (tokenizer.hasMoreTokens())
    {
      String dirLevel = tokenizer.nextToken() + separator;

      thePath.append(dirLevel);

      BreadCrumb breadCrumb = new BreadCrumb(new File(thePath.toString()), dirLevel, this);

      add(breadCrumb);
      breadCrumb.addMouseListener(breadCrumb);
    }

    add(browseButton);
    invalidate();
  }
}
