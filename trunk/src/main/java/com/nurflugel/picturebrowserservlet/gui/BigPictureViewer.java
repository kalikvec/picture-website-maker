package com.nurflugel.picturebrowserservlet.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

class BigPictureViewer extends JDialog implements MouseListener
{
  /** Creates a new BigPictureViewer object. */
  BigPictureViewer(String text, MainFrame frame)
  {
    super(frame, text, true);

    ImageIcon         icon    = new ImageIcon(text);
    ScrollablePicture picture = new ScrollablePicture(icon, 2);

    picture.addMouseListener(this);

    JScrollPane scrollPane = new JScrollPane(picture);

    getContentPane().add(scrollPane);

    Toolkit   toolkit    = Toolkit.getDefaultToolkit();
    Dimension screenSize = toolkit.getScreenSize();

    setSize(screenSize);
  }

  // ------------------------ INTERFACE METHODS ------------------------
  // --------------------- Interface MouseListener ---------------------
  /**  */
  public void mouseClicked(MouseEvent event)
  {
    dispose();
  }

  /**  */
  public void mousePressed(MouseEvent event) {}

  /**  */
  public void mouseReleased(MouseEvent event) {}

  /**  */
  public void mouseEntered(MouseEvent event) {}

  /**  */
  public void mouseExited(MouseEvent event) {}
}
