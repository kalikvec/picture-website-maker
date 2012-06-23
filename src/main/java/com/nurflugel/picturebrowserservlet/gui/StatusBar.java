package com.nurflugel.picturebrowserservlet.gui;

import javax.swing.*;
import java.awt.*;

class StatusBar extends JPanel
{
  private JLabel theTextLabel = new JLabel();

  /** Creates a new StatusBar object. */
  StatusBar()
  {
    setLayout(new FlowLayout());
    theTextLabel.setText("");
    add(theTextLabel);
    invalidate();
  }
  // ------------------------ OTHER METHODS ------------------------

  /**  */
  public void clear()
  {
    theTextLabel.setText("");
    invalidate();
  }

  /**  */
  public void setText(String text)
  {
    theTextLabel.setText(text);
    invalidate();
  }
}
