package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import org.apache.log4j.Category;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import static java.awt.event.KeyEvent.VK_ENTER;
import static java.awt.event.KeyEvent.VK_TAB;
import static javax.swing.SwingConstants.CENTER;

class DirPanel extends JPanel implements MouseListener
{
  /** Use serialVersionUID for interoperability. */
  private static final long     serialVersionUID      = 4329025516583899295L;
  private MainFrame             theFrame              = null;
  private JLabel                theTitle              = null;
  private JTextArea             textArea              = null;
  private Dir                   dir                   = null;
  private SimpleDateFormat      dateFormat            = new SimpleDateFormat("hh:mm:ss:SS");
  private ThumbnailReaderWriter thumbnailReaderWriter = null;
  private Category              logger                = LogFactory.getInstance(DirPanel.class);

  /** Creates a new DirPanel object. */
  DirPanel(MainFrame theFrame, Dir dir)
  {
    this.theFrame = theFrame;
    this.dir      = dir;
    setup();
  }

  /**  */
  private void setup()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("DirPanel.setup " + getFormattedDateTime());
    }

    setBorder(new EtchedBorder());

    JLabel theLabel = new JLabel(dir.getFile().getName());

    theLabel.addMouseListener(this);
    textArea = new JTextArea(1, 10);
    textArea.setLineWrap(true);
    textArea.setText(dir.getDescription());
    textArea.addKeyListener(new KeyAdapter()
      {
        @Override
        public void keyPressed(KeyEvent event)
        {
          int keyCode = event.getKeyCode();

          if (keyCode == VK_ENTER)
          {
            setTextFromTextArea();
          }
          else if (keyCode == VK_TAB)
          {
            getParent().transferFocus();
          }
        }
      });
    textArea.addFocusListener(new FocusAdapter()
      {
        @Override
        public void focusLost(FocusEvent e)
        {
          setTextFromTextArea();
        }
      });
    theTitle.setHorizontalAlignment(CENTER);
    theTitle.setAlignmentX(0.5f);
    setLayout(new FlowLayout());

    // setLayout(new GridLayout(1, 2));
    add(theLabel);
    add(textArea);
  }

  /**  */
  private String getFormattedDateTime()
  {
    Date date = new Date();

    return dateFormat.format(date);
  }

  /**  */
  private void setTextFromTextArea()
  {
    String text = textArea.getText();

    dir.setDescription(text);

    if (logger.isDebugEnabled())
    {
      logger.debug("text = " + text);
    }
  }

  // ------------------------ INTERFACE METHODS ------------------------
  // --------------------- Interface MouseListener ---------------------
  public void mouseClicked(MouseEvent event)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Open the picture!!!");
    }

    theFrame.setCurrentDir(dir.getFile(), true);
  }

  /**  */
  public void mousePressed(MouseEvent event) {}

  /**  */
  public void mouseReleased(MouseEvent event) {}

  /**  */
  public void mouseEntered(MouseEvent event) {}

  /**  */
  public void mouseExited(MouseEvent event) {}

  // ------------------------ OTHER METHODS ------------------------
  public void setSizeInConcrete()
  {
    setMaximumSize(getSize());
    setPreferredSize(getSize());
  }
}
