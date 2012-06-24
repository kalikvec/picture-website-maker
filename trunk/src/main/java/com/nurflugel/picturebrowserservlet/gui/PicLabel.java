package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.util.UtilMethods;
import com.nurflugel.picturebrowserservlet.domain.GraphicFile;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import com.nurflugel.picturebrowserservlet.exif.NurTag;
import com.nurflugel.picturebrowserservlet.exif.NurTagFilter;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.util.List;
import org.apache.log4j.Category;
import static java.awt.datatransfer.DataFlavor.stringFlavor;

@SuppressWarnings({ "RefusedBequest" })
public class PicLabel extends JPanel implements ActionListener, DropTargetListener, DragGestureListener, DragSourceListener, MouseListener
{
  private static final Category logger                = LogFactory.getInstance(PicLabel.class);
  private JLabel                thePicture;
  private JLabel                theTitle;
  private JTextArea             textArea;
  private JComboBox             theDropdown;
  private Icon                  icon;
  private MainFrame             theFrame;
  private MediaFile             thePic;
  private SimpleDateFormat      dateFormat            = new SimpleDateFormat("hh:mm:ss:SS");
  private ThumbnailReaderWriter thumbnailReaderWriter;
  private JPopupMenu            popupMenu;
  private PicPanel              picPanel;
  private ClassLoader           classloader;
  private Border                unselectedBorder      = new LineBorder(Color.BLACK);
  private boolean               isSelected;
  private JCheckBoxMenuItem     checkMenuItem;

  /** Creates a new PicLabel object. */
  public PicLabel(MediaFile thePic, MainFrame mainFrame, PicPanel picPanel)
  {
    classloader   = getClass().getClassLoader();
    this.picPanel = picPanel;

    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.PicLabel");
    }

    this.thePic = thePic;
    theFrame    = mainFrame;
    setFocusable(true);

    File file = thePic.getFile();

    // Create the popupMenu menu.
    popupMenu = new JPopupMenu();

    JMenuItem menuItem = new JMenuItem("Rename");

    menuItem.addActionListener(this);
    popupMenu.add(menuItem);
    menuItem = new JMenuItem("Delete");
    menuItem.addActionListener(this);
    popupMenu.add(menuItem);
    menuItem = new JMenuItem("Cut");
    menuItem.addActionListener(this);
    popupMenu.add(menuItem);
    menuItem = new JMenuItem("Paste");
    menuItem.addActionListener(this);
    popupMenu.add(menuItem);
    checkMenuItem = new JCheckBoxMenuItem("Show EXIF dropdown");
    checkMenuItem.addActionListener(this);
    checkMenuItem.setVisible(thePic.shouldDisplayExifDropdown());
    checkMenuItem.setSelected(thePic.shouldDisplayExifDropdown());
    popupMenu.add(checkMenuItem);

    if (logger.isDebugEnabled())
    {
      // menuItem = new JMenuItem("Hide File (don't show on page)");
      // menuItem.addActionListener(this);
      // popupMenu.add(menuItem);
      logger.debug("Making an icon for " + file + "\ttime= " + getFormattedDateTime());
    }

    DragSource dragSource = DragSource.getDefaultDragSource();

    // DragGestureRecognizer gestureRecognizer = dragSource.createDefaultDragGestureRecognizer(this,
    // DnDConstants.ACTION_COPY_OR_MOVE, this);              DropTarget            dropTarget        = new DropTarget(this, this);
    dragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
    new DropTarget(this, this);

    try
    {
      icon = getThumbnailIcon(file);
      setup();
    }
    catch (Exception e)
    {
      logger.error("PicLabel.PicLabel - error decoding image ", e);
    }
  }

  /**  */
  private String getFormattedDateTime()
  {
    Date date = new Date();

    return dateFormat.format(date);
  }

  /**  */
  private Icon getThumbnailIcon(File sourceFile) throws IOException
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.getThumbnailIcon");
    }

    String    fileName    = sourceFile.getName();
    ImageIcon smallerIcon = null;

    if (!fileName.toLowerCase().contains("_sm.jpg"))
    {
      thumbnailReaderWriter = MainFrame.getThumbnailHandler();

      File thumbnailFile;

      if (UtilMethods.isMp3(fileName))
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("PicLabel.getThumbnailIcon - is mp3");
        }

        try
        {
          smallerIcon = new ImageIcon(classloader.getResource(TagsAndStuff.SOUND_ICON));
        }
        catch (Exception e)
        {
          thumbnailFile = new File("images/" + TagsAndStuff.SOUND_ICON);
          smallerIcon   = thumbnailReaderWriter.getOrCreateImageFromFile(thumbnailFile, sourceFile);
        }
      }
      else if (UtilMethods.isAnimation(fileName))
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("PicLabel.getThumbnailIcon - is animation");
        }

        try
        {
          smallerIcon = new ImageIcon(classloader.getResource(TagsAndStuff.VIDEO_ICON));
        }
        catch (Exception e)
        {
          thumbnailFile = new File("images/" + TagsAndStuff.VIDEO_ICON);
          smallerIcon   = thumbnailReaderWriter.getOrCreateImageFromFile(thumbnailFile, sourceFile);
        }
      }
      else  // graphic
      {
        thumbnailFile = thumbnailReaderWriter.getThumbnailFile(sourceFile);
        smallerIcon   = thumbnailReaderWriter.getOrCreateImageFromFile(thumbnailFile, sourceFile);
      }
    }

    return smallerIcon;
  }
  // todo - put this into layout?

  /**  */
  private void setup()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.setup " + getFormattedDateTime());
    }

    setBorder(unselectedBorder);
    thePicture = new JLabel(icon);
    theTitle   = new JLabel(thePic.getFile().getName());
    textArea   = new JTextArea(4, 10);
    textArea.setLineWrap(true);
    textArea.setText(thePic.getDescription());
    textArea.setFocusable(true);
    addListeners();
    theTitle.setHorizontalAlignment(SwingConstants.CENTER);
    theTitle.setAlignmentX(0.5f);

    JPanel bottomPanel = new JPanel();

    setLayout(new BorderLayout());
    add(thePicture, BorderLayout.CENTER);
    add(bottomPanel, BorderLayout.SOUTH);
    bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
    bottomPanel.add(theTitle);
    theDropdown = getExifStuffForDropdown();

    if (theFrame.showExifInfoCheckBox())
    {
      bottomPanel.add(theDropdown);
    }

    // if (theFrame.showExifInfoCheckBox())
    // {
    checkMenuItem.setVisible(true);

    // }
    bottomPanel.add(textArea);
    thePicture.setOpaque(true);  // MUST do this for background to show up.
  }

  /**  */
  private void addListeners()
  {
    addMouseListener(this);
    textArea.addKeyListener(new KeyAdapter()
      {
        public void keyPressed(KeyEvent event)
        {
          int keyCode = event.getKeyCode();
          int vkEnter = KeyEvent.VK_ENTER;
          int vkTab   = KeyEvent.VK_TAB;

          if (keyCode == vkEnter)
          {
            String text = textArea.getText();

            thePic.setDescription(text);

            if (logger.isDebugEnabled())
            {
              logger.debug("text = " + text);
            }
          }
          else if (keyCode == vkTab)
          {
            getParent().transferFocus();
          }
        }
      });
    textArea.addFocusListener(new FocusAdapter()
      {
        public void focusLost(FocusEvent e)
        {
          String text = textArea.getText();

          thePic.setDescription(text);

          if (logger.isDebugEnabled())
          {
            logger.debug("text = " + text);
          }
        }
      });
  }

  /**  */
  @SuppressWarnings({ "CollectionDeclaredAsConcreteClass", "UseOfObsoleteCollectionType" })
  private JComboBox getExifStuffForDropdown()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.getExifStuffForDropdown");
    }

    NurTagFilter nurTagFilter    = new NurTagFilter(thePic.getFile());
    List<NurTag> interestingTags = nurTagFilter.getInterestingTags();
    String       topTag          = NurTag.getTopTag(interestingTags);
    NurTag       topNurTag       = new NurTag();

    topNurTag.setTagValue(topTag);

    Vector<NurTag> tags = new Vector<NurTag>();

    tags.addAll(interestingTags);
    tags.add(0, topNurTag);

    JComboBox newDropdown = new JComboBox(tags);

    newDropdown.setMaximumRowCount(20);

    return newDropdown;
  }
  // --------------------- Interface ActionListener ---------------------

  /**  */
  public void actionPerformed(ActionEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("Action performed!!!!!!!!!!!!!!!!!!!!!!!!!!!!!:  " + e);
    }

    String actionCommand = e.getActionCommand();

    if ("Delete".equals(actionCommand))
    {
      deleteAction();
    }
    else if ("Rename".equals(actionCommand))
    {
      renameAction();
    }
    else if ("Cut".equals(actionCommand))
    {
      picPanel.cutAction();
    }
    else if ("Paste".equals(actionCommand))
    {
      picPanel.pasteAction(this);
    }
    else if ("Show EXIF dropdown".equals(actionCommand))
    {
      boolean selected = checkMenuItem.isSelected();

      thePic.setDisplayExifDropdown(selected);
      theDropdown.setVisible(selected);
    }
  }
  // --------------------- Interface DragGestureListener ---------------------

  /**  */
  public void dragGestureRecognized(DragGestureEvent dge)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragGestureRecognized");
    }

    String       fileName     = thePic.getFile().getName();
    Transferable transferable = new FileListTransferable(fileName);

    dge.startDrag(null, transferable);
  }
  // --------------------- Interface DragSourceListener ---------------------

  /**  */
  public void dragEnter(DragSourceDragEvent dsde)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragEnter");
    }

    lowerBorder();
  }

  /**  */
  public void dragOver(DragSourceDragEvent dsde)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragOver");
    }
  }

  /**  */
  public void dropActionChanged(DragSourceDragEvent dsde) {}

  /**  */
  public void dragExit(DragSourceEvent dse)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragExit");
    }

    raiseBorder();
  }

  /**  */
  public void dragDropEnd(DragSourceDropEvent event)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragDropEnd");
    }

    if (event.getDropSuccess())
    {
      int action = event.getDropAction();

      if (action == DnDConstants.ACTION_MOVE)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("PicLabel.dragDropEnd - MOVING!!!");
        }

        Object source = event.getSource();

        if (logger.isDebugEnabled())
        {
          logger.debug("source = " + source);
        }

        // DragSourceContext dragSourceContext = event.getDragSourceContext();
        // DragSource dragSource = event.getDragSourceContext().getDragSource();
        event.getDragSourceContext();

        DragSource dragSource = event.getDragSourceContext().getDragSource();

        if (logger.isDebugEnabled())
        {
          logger.debug("dragSource = " + dragSource);
        }
      }
    }
  }
  // --------------------- Interface DropTargetListener ---------------------

  /**  */
  public void dragEnter(DropTargetDragEvent dtde)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragEnter");
    }

    lowerBorder();
  }

  /**  */
  public void dragOver(DropTargetDragEvent dtde)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragOver");
    }

    double x = dtde.getLocation().getX();
    double y = dtde.getLocation().getY();

    if (logger.isDebugEnabled())
    {
      logger.debug("x = " + x);
      logger.debug("y = " + y);
    }

    picPanel.reportPosition(y, this);
  }

  /**  */
  public void dropActionChanged(DropTargetDragEvent dtde)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dropActionChanged");
    }
  }

  /** Do cleanup if necessary. */
  public void dragExit(DropTargetEvent dte)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.dragExit");
    }

    raiseBorder();
  }

  /**  */
  public void drop(DropTargetDropEvent event)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.drop");
    }

    event.acceptDrop(DnDConstants.ACTION_MOVE);

    Transferable transferable = event.getTransferable();
    DataFlavor[] flavors      = transferable.getTransferDataFlavors();

    for (DataFlavor flavor : flavors)
    {
      if (flavor.equals(stringFlavor))
      {
        try
        {
          FileListTransferable transferData = (FileListTransferable) transferable.getTransferData(flavor);
          String               fileName     = transferData.getFileName();

          if (logger.isDebugEnabled())
          {
            logger.debug("PicLabel.drop source filename = " + fileName);
          }

          String targetFileName = thePic.getFile().getName();

          if (logger.isDebugEnabled())
          {
            logger.debug("PicLabel.drop this filename= " + targetFileName);
          }

          picPanel.movePic(fileName, targetFileName);
        }
        catch (Exception e)
        {
          logger.error("error", e);
        }
      }
    }

    event.dropComplete(true);
  }
  // --------------------- Interface MouseListener ---------------------

  /**  */
  public void mouseClicked(MouseEvent event)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.mouseClicked");
    }

    requestFocusInWindow();

    int     x                              = event.getX();
    int     y                              = event.getY();
    Icon    icon                           = thePicture.getIcon();
    int     iconHeight                     = icon.getIconHeight();
    int     iconWidth                      = icon.getIconWidth();
    int     labelWidth                     = thePicture.getWidth();
    int     minx                           = (labelWidth - iconWidth) / 2;
    int     maxx                           = minx + iconWidth;
    int     titleHeight                    = theTitle.getHeight();
    int     dropdownHeight                 = theDropdown.getHeight();
    int     labelheight                    = getHeight();
    int     textboxHeight                  = textArea.getHeight();
    int     bottomBorderWidth              = (labelheight - titleHeight - dropdownHeight - iconHeight - textboxHeight) / 2;
    int     miny                           = bottomBorderWidth;
    int     maxy                           = miny + iconHeight;
    boolean isClickWithinPicture           = (x > minx) && (x < maxx) && (y > miny) && (y < maxy);
    int     button                         = event.getButton();  // is it the picture we just clicked on?
    int     onmask                         = InputEvent.CTRL_DOWN_MASK;
    int     modifiersEx                    = event.getModifiersEx();
    boolean isControlKeyAndMouseButtonDown = (modifiersEx & onmask) == onmask;

    if (button == MouseEvent.BUTTON3)
    {
      if (!isSelected)
      {
        invertPictureStatus();
      }

      popupMenu.show(event.getComponent(), x, y);
    }
    else if (button == MouseEvent.BUTTON1)
    {
      if (isControlKeyAndMouseButtonDown)
      {
        invertPictureStatus();
      }
      else if (isClickWithinPicture)
      {
        executeFile();
      }
      else
      {
        picPanel.unselectAllOtherPics(this);
        invertPictureStatus();
      }
    }
    else {}
  }

  /**  */
  public void mousePressed(MouseEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.mousePressed " + thePic.getFile().getName());
    }
  }

  /**  */
  public void mouseReleased(MouseEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.mouseReleased " + thePic.getFile().getName());
    }
  }

  /**  */
  public void mouseEntered(MouseEvent event) {}

  /**  */
  public void mouseExited(MouseEvent event) {}

  // ------------------------ Class Methods ------------------------
  /**  */
  void delete()
  {
    File file     = thePic.getFile();
    int  response = JOptionPane.showConfirmDialog(this, "Really delete this picture?", "There's no going back!", JOptionPane.YES_NO_OPTION);

    if (response == JOptionPane.YES_OPTION)
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("response = " + response);
      }

      file.delete();
      picPanel.removeFile(this);
    }
  }

  /**  */
  private void deleteAction()
  {
    picPanel.deleteAction();
  }

  /**  */
  @SuppressWarnings({ "CallToRuntimeExec" })
  private void executeFile()
  {
    String absolutePath = thePic.getFile().getAbsolutePath();

    if (theFrame.shouldWritePreviews())
    {
      absolutePath = new File(thePic.getFile().getParentFile().getPath() + File.separatorChar + "previews", thePic.getFile().getName())
                       .getAbsolutePath();
    }

    if (thePic instanceof GraphicFile)
    {
      new BigPictureViewer(absolutePath, theFrame).show();
    }
    else  // let the system deal with it
    {
      try
      {
        Runtime.getRuntime().exec("cmd.exe /c \"" + absolutePath + "\"");
      }
      catch (IOException e)
      {
        logger.error("Error ", e);
      }
    }
  }

  /**  */
  public void focusGained(FocusEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.focusGained");
    }

    lowerBorder();
    invalidate();
  }

  /**  */
  private void lowerBorder()
  {
    thePicture.setBackground(Color.DARK_GRAY);
  }

  /**  */
  public void focusLost(FocusEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.focusLost");
    }

    raiseBorder();
    invalidate();
  }

  /**  */
  public void raiseBorder()
  {
    try
    {
      if (thePicture != null)
      {
        thePicture.setBackground(Color.LIGHT_GRAY);
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**  */
  private void invertPictureStatus()
  {
    isSelected = !isSelected;
    changeBorderToShowSelectedStatus();
  }

  /**  */
  private void changeBorderToShowSelectedStatus()
  {
    if (thePicture != null)
    {
      if (isSelected)
      {
        thePicture.setBackground(Color.DARK_GRAY);
      }
      else
      {
        thePicture.setBackground(Color.LIGHT_GRAY);
      }

      repaint();
    }
  }

  /**  */
  public void keyPressed(KeyEvent e) {}

  /**  */
  public void keyReleased(KeyEvent e) {}

  /**  */
  public void keyTyped(KeyEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicLabel.keyTyped");
    }

    boolean controlDown = e.isControlDown();
    int     keyCode     = e.getKeyCode();

    if (controlDown)
    {
      if (keyCode == KeyEvent.VK_X)
      {
        picPanel.cutAction();
      }

      if (keyCode == KeyEvent.VK_V)
      {
        picPanel.pasteAction(this);
      }
    }
  }

  /**  */
  void rename()
  {
    File   file        = thePic.getFile();
    String oldFileName = file.getName();
    String newFileName = (String) JOptionPane.showInputDialog(this, "What is the new file name?", "Rename File", JOptionPane.PLAIN_MESSAGE, icon,
                                                              null, oldFileName);

    theTitle.setText(newFileName);

    if ((newFileName != null) && !newFileName.equals(file.getName()))
    {
      File newFile          = new File(file.getParent(), newFileName);
      File oldThumbnailFile = thumbnailReaderWriter.getThumbnailFile(file);
      File newThumbnailFile = thumbnailReaderWriter.getThumbnailFile(newFile);

      if (logger.isDebugEnabled())
      {
        logger.debug("Reanming   " + file.getAbsolutePath() + " to " + newFile.getAbsolutePath());
      }

      try
      {
        renameFile(file, newFile);
        renameFile(oldThumbnailFile, newThumbnailFile);
        thePic.setFile(newFile);
      }
      catch (IOException e1)
      {
        logger.error("error ", e1);
      }

      repaint();

      if (logger.isDebugEnabled())
      {
        logger.debug("newFileName = " + newFileName);
      }
    }
  }

  /**  */
  private void renameFile(File inputFile, File outputFile) throws IOException
  {
    copyFile(inputFile, outputFile);
    inputFile.delete();
  }

  /** Copies a file from one place to another. */
  private static void copyFile(File inputFile, File outputFile) throws IOException
  {
    FileInputStream  in  = null;
    FileOutputStream out = null;

    try
    {
      in  = new FileInputStream(inputFile);
      out = new FileOutputStream(outputFile);

      int    c;
      byte[] b = new byte[1024];

      while ((c = in.read(b)) != -1)
      {
        out.write(b, 0, c);
      }
    }
    finally
    {
      try
      {
        if (in != null)
        {
          in.close();
        }
      }
      finally
      {
        if (out != null)
        {
          out.close();
        }
      }
    }
  }

  /**  */
  private void renameAction()
  {
    picPanel.renameAction();
  }

  /**  */
  void setSelected(boolean isSelected)
  {
    this.isSelected = isSelected;
    changeBorderToShowSelectedStatus();
  }

  /**  */
  public void setShowExifDropdown(boolean selected)
  {
    if (theDropdown != null)
    {
      theDropdown.setVisible(selected);
      thePic.setDisplayExifDropdown(selected);
    }

    checkMenuItem.setSelected(selected);
  }

  /**  */
  public void setSizeInConcrete(Dimension size)
  {
    setMaximumSize(size);
    setPreferredSize(size);
  }
  // ------------------------ GETTER/SETTER METHODS ------------------------

  /**  */
  public MediaFile getThePic()
  {
    return thePic;
  }

  /**  */
  public Icon getIcon()
  {
    return thePicture.getIcon();
  }

  /**  */
  public String getText()
  {
    return thePicture.getText();
  }

  /**  */
  public boolean isSelected()
  {
    return isSelected;
  }
}
