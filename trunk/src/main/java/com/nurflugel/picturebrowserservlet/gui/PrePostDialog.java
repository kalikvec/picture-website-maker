package com.nurflugel.picturebrowserservlet.gui;

import javax.swing.*;
import java.awt.event.*;

/** Created with IntelliJ IDEA. User: douglas_bullard Date: 4/8/12 Time: 19:17 To change this template use File | Settings | File Templates. */
public class PrePostDialog extends JDialog
{
  private JButton   okButton;
  private JButton   cancelButton;
  private JTextArea preTextArea;
  private JTextArea postTextArea;
  private JPanel    contentPane;
  private boolean   wasCancelled;

  public PrePostDialog()
  {
    setContentPane(contentPane);
    setModal(true);
    getRootPane().setDefaultButton(okButton);
    okButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          onOK();
        }
      });
    cancelButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          onCancel();
        }
      });

    // call onCancel() when cross is clicked
    setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    addWindowListener(new WindowAdapter()
      {
        public void windowClosing(WindowEvent e)
        {
          onCancel();
        }
      });

    // call onCancel() on ESCAPE
    contentPane.registerKeyboardAction(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          onCancel();
        }
      }, KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
    pack();
    setVisible(true);
  }

  private void onOK()
  {
    // add your code here
    dispose();
  }

  private void onCancel()
  {
    // add your code here if necessary
    wasCancelled = true;
    dispose();
  }

  public String getPreBodyText()
  {
    return preTextArea.getText();
  }

  public String getPostBodyText()
  {
    return postTextArea.getText();
  }

  public boolean wasCanceled()
  {
    return wasCancelled;
  }

  public static void main(String[] args)
  {
    PrePostDialog dialog = new PrePostDialog();

    dialog.pack();
    dialog.setVisible(true);
    System.exit(0);
  }
}
