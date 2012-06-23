package com.nurflugel.picturebrowserservlet.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TestDialog extends JFrame
{
  // ------------------------ OTHER METHODS ------------------------
  public void doIt()
  {
    Container    contentPane   = getContentPane();
    MyPanel      thePanel      = new MyPanel();
    MyOtherPanel theOtherPanel = new MyOtherPanel();

    pack();
  }

  static class MyPanel extends JPanel
  {
    JLabel     theLabel;
    JButton    theButton;
    JTextField theField;

    MyPanel()
    {
      theButton.addActionListener(new ActionListener()
        {
          public void actionPerformed(ActionEvent e)
          {
            theField.setText("Suck me");
          }
        });
    }
  }

  class MyOtherPanel extends JPanel
  {
    public MyOtherPanel() {}
  }

  /**  */
  public static void main(String[] args)
  {
    TestDialog testDialog = new TestDialog();

    testDialog.show();
  }
}
