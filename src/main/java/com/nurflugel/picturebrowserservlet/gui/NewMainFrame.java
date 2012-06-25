package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import com.nurflugel.picturebrowserservlet.htmlstuff.HtmlWriter;
import com.nurflugel.picturebrowserservlet.htmlstuff.Skin;
import com.nurflugel.picturebrowserservlet.settings.Settings;
import com.nurflugel.util.Util;
import org.apache.log4j.Category;
import javax.help.CSH;
import javax.help.HelpSet;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import static com.nurflugel.picturebrowserservlet.gui.MetadataWriter.saveMetadata;
import static com.nurflugel.picturebrowserservlet.gui.SortCriteria.FileName;
import static com.nurflugel.picturebrowserservlet.gui.SortCriteria.FileTimestamp;
import static com.nurflugel.picturebrowserservlet.gui.SortCriteria.ImageTimestamp;
import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.metadataFileName;
import static com.nurflugel.picturebrowserservlet.gui.ThumbnailReaderWriterFactory.getThumbnailReaderWriter;
import static com.nurflugel.picturebrowserservlet.util.UtilMethods.getNumRowsFromString;
import static com.nurflugel.util.Util.VERSION;
import static com.nurflugel.util.Util.center;
import static com.nurflugel.util.Util.setLookAndFeel;
import static java.awt.Cursor.getPredefinedCursor;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.event.KeyEvent.VK_DELETE;
import static java.awt.event.KeyEvent.VK_V;
import static java.awt.event.KeyEvent.VK_X;

/** Created with IntelliJ IDEA. User: douglas_bullard Date: 6/24/12 Time: 17:01 To change this template use File | Settings | File Templates. */
public class NewMainFrame extends JFrame implements KeyListener, MainFrameInterface
{
  private static Category           logger                             = LogFactory.getInstance(NewMainFrame.class);
  private static final int          DEFAULT_NUM_COLUMNS                = 4;
  private NewMainFrame              frame;
  private Cursor                    normalCursor                       = Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR);
  private Cursor                    waitCursor                         = Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR);
  private JComboBox                 numberOfRowsComboBox;
  private JComboBox                 numberOfColumnsComboBox;
  private JCheckBox                 enablePrePostBODYCheckBox;
  private JRadioButton              create_smFilesInRadioButton;
  private JRadioButton              makeThumbnailsDirectoryRadioButton;
  private JCheckBox                 addUpIconCheckBox;
  private JCheckBox                 showEXIFInfoInCheckBox;
  private JCheckBox                 generatePreviewImages1024CheckBox;
  private JRadioButton              fileNameRadioButton;
  private JRadioButton              pictureDateRadioButton;
  private JRadioButton              forwardsRadioButton;
  private JRadioButton              reverseRadioButton;
  private JButton                   saveXMLButton;
  private JButton                   saveHTMLButton;
  private JButton                   quitButton;
  private BreadCrumbPanel           breadCrumbPanel;
  private StatusBar                 statusBar;
  private JPanel                    mainPanel;
  private PictureScrollPane         pictureScrollPane;
  private ThumbnailReaderWriter     thumbnailReaderWriter;
  private Toolkit                   toolkit                            = getDefaultToolkit();
  private Dimension                 screenSize                         = toolkit.getScreenSize();
  private Dir                       dirpage;
  private DirTable                  dirTable;
  private File                      currentDir;  // = new File(TagsAndStuff.defaultDirName);
  private HtmlWriter                htmlWriter;
  private int                       numColumns                         = DEFAULT_NUM_COLUMNS;
  private int                       numRows                            = 4;
  private boolean                   useGlassPane                       = true;
  private InfiniteProgressPanel     glassPane;
  private transient ExecutorService executor;
  private Settings                  settings;
  private ProgressMonitor           progressMonitor;
  private PicContainer              picturePanel;
  private boolean                   isInitialUse                       = true;
  private Skin[]                    availableSkins;
  private Skin                      skin;

  public NewMainFrame() throws HeadlessException
  {
    frame = this;
    frame.setContentPane(mainPanel);
    initializeUi();
    addListeners();
  }

  public void createUIComponents() {}

  private void initializeUi()
  {
    setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel", frame);
    frame.pack();
    center(frame);
    frame.setTitle("Picture Website Maker v" + VERSION);
    frame.setVisible(true);
  }

  /**  */
  private void addListeners()
  {
    addKeyListener(this);
    addWindowListener(new WindowAdapter()
      {
        @Override
        public void windowClosing(WindowEvent evt)
        {
          exitForm();
        }
      });
    enablePrePostBODYCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          handlePrePostBodyCheck();
        }
      });
    quitButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          exitForm();
        }
      });
    saveHTMLButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          saveHtml();
        }
      });
    saveXMLButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          if (logger.isDebugEnabled())
          {
            logger.debug("MainFrame.saveMetadata");
          }

          saveMetaData();
        }
      });
    makeThumbnailsDirectoryRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          thumbnailReaderWriter = getThumbnailReaderWriter(makeThumbnailsDirectoryRadioButton.isSelected());
        }
      });
    create_smFilesInRadioButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          thumbnailReaderWriter = getThumbnailReaderWriter(create_smFilesInRadioButton.isSelected());
        }
      });
    showEXIFInfoInCheckBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setExifInfoTasks();
        }
      });

    // exitMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // exitForm();
    // }
    // });
    numberOfRowsComboBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          numRows = getNumRowsFromDropdown();
        }
      });
    numberOfColumnsComboBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          numberOfColumnsDropdownSelected();
        }
      });

    // aboutMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // showAboutDialog();
    // }
    // });
    // saveHtmlMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // saveHtml();
    // }
    // });
    // saveXmlMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // saveMetaData();
    // }
    // });
    // defaultSkinMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // setSkin("default");
    // }
    // });
    // wingSkinMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // setSkin("wing");
    // }
    // });
    // cutMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // cutAction();
    // }
    // });
    // pasteMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // pasteAction();
    // }
    // });
    // deleteMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // deleteAction();
    // }
    // });
    // renameMenuItem.addActionListener(new ActionListener()
    // {
    // public void actionPerformed(ActionEvent evt)
    // {
    // showHelpStuff();  // todo wtf???
    // }
    // });
    try
    {
      URL url = getClass().getClassLoader().getResource("help.hs");

      if (url == null)
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("Tried to get help.hs as a URL, returned null");
        }

        url = new URL("file:src/app/web/help/help.hs");

        if (logger.isDebugEnabled())
        {
          logger.debug("url is now = " + url);
        }
      }

      CSH.DisplayHelpFromSource displayHelpFromSource = new CSH.DisplayHelpFromSource(new HelpSet(null, url).createHelpBroker());

      // contentsMenuItem.addActionListener(displayHelpFromSource);
    }
    catch (Exception e)
    {
      logger.error("error", e);
    }
  }

  private void handlePrePostBodyCheck()
  {
    // todo something
  }

  /** Exit the Application. */
  private void exitForm()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.exitForm");
    }

    if (dirpage != null)
    {
      saveMetadata(currentDir, dirpage, numColumns, numRows, addUpIconCheckBox.isSelected());
    }

    saveSettings();
    System.exit(0);
  }

  void saveSettings()
  {                                                        // add setting settings in listeners...

    Settings settings     = new Settings();
    File     currentDir1  = currentDir;
    File     parentFile   = currentDir1.getParentFile();
    String   absolutePath = parentFile.getAbsolutePath();  // todo this is null if the file doesn't exist...

    settings.setLastVisitedDir(absolutePath);              // todo npe here
    settings.setNumColumns(getNumColumnsFromDropdown());
    settings.setNumRows(getNumRowsFromDropdown());
    settings.setShowExifInfo(showExifInfoCheckBox());
    settings.setShowPreviews(shouldWritePreviews());
    settings.save();
  }

  /**  */
  private int getNumColumnsFromDropdown()
  {
    return getNumRowsFromString((String) numberOfColumnsComboBox.getSelectedItem());
  }

  /**  */
  public boolean showExifInfoCheckBox()
  {
    return showEXIFInfoInCheckBox.isSelected();
  }

  public boolean shouldWritePreviews()
  {
    return generatePreviewImages1024CheckBox.isSelected();
  }

  /**  */
  private void saveHtml()
  {
    getDirsFromTable();

    try
    {
      htmlWriter.saveHtml(skin);
    }
    catch (IOException e)
    {
      logger.error("error", e);
    }
  }

  /**  */
  private void getDirsFromTable()
  {
    int rowCount = dirTable.getRowCount();

    for (int row = 0; row < rowCount; row++)
    {
      Dir    dir  = dirTable.getDirAt(row);
      String text = dirTable.getDescriptionAt(row);

      dir.setDescription(text);
    }
  }

  /**  */
  private void saveMetaData()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.saveMetaData");
    }

    getDirsFromTable();
    saveMetadata(currentDir, dirpage, numColumns, numRows, addUpIconCheckBox.isSelected());
  }

  /**  */
  private void setExifInfoTasks()
  {
    boolean selected = showEXIFInfoInCheckBox.isSelected();

    setExifUiSettings(selected);
  }

  private void setExifUiSettings(boolean selected)
  {
    dirpage.setDisplayExifDropdown(selected);
    picturePanel.showExifDropdowns(selected);
  }

  private int getNumRowsFromDropdown()
  {
    return getNumRowsFromString((String) numberOfRowsComboBox.getSelectedItem());
  }

  /**  */
  private void numberOfColumnsDropdownSelected()
  {
    numColumns = getNumColumnsFromDropdown();
    refresh(false);
  }

  /** Refresh the display with the contents of the current directory. */
  private void refresh(boolean getNumColumnsFromXml)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.refresh numColumns = " + numColumns);
    }

    setUpDirPage(getNumColumnsFromXml);

    List<Dir> dirs = (dirpage == null) ? new ArrayList<Dir>()
                                       : dirpage.getDirs();
    List<MediaFile> pics = (dirpage == null) ? new ArrayList<MediaFile>()
                                             : dirpage.getPics();

    Collections.sort(dirs);  // do an initial sort by dir
    Collections.sort(pics);  // do an initial sort by file

    // timer.start();
    setTheCursor(waitCursor);

    if (useGlassPane && !pics.isEmpty())
    {
      glassPane.start();
    }

    setNumColumnButtons();
    getInfoFromXmlFile(getNumColumnsFromXml);
    dirTable.refresh(dirs);

    // progressMonitor = new ProgressMonitor(this, "Making previews images...", "Preview image status", 0, pics.size());
    // progressMonitor.setProgress(0);
    // progressMonitor.setMillisToDecideToPopup(1 / 2);
    PreviewGenerator generator = new PreviewGenerator(pics, progressMonitor, this);

    if (shouldWritePreviews())  // executor.execute(generator);
    {
      generator.run();
      // EventQueue.invokeLater(new Runnable() {
      //
      // public void run() {
      //
      // try {
      //
      // if (!pics.isEmpty()) {                                File previewDir = new File(pics.get(0).getFile().getParentFile(), "previews");
      //
      // if (!previewDir.exists()) {                                    previewDir.mkdir();       }
      //
      // ScaledImageWriter imageWriter = new ScaledImageWriter();
      //
      // for (MediaFile pic : pics) {                                    String previewFileName = pic.getFile().getName(); File   scaledFile      =
      // new File(previewDir, previewFileName);
      //
      // imageWriter.getOrCreateImageFromFile(scaledFile, pic.getFile());                                } }                        } catch
      // (IOException e) { // logger.error("error", e);                        }          } });
    }

    picturePanel.refresh(pics, numColumns, dirpage);
    pictureScrollPane.getVerticalScrollBar().setValue(0);

    // timer.stop();
    setTheCursor(normalCursor);
    enableButtons(true);
  }

  /** Reads in any files from the page and puts them into the page. */
  private void setUpDirPage(boolean refreshFromXml)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.setUpDirPage");
    }

    File            dirFile     = null;
    String          title       = "";
    String          description = "";
    String          url         = "";
    String          introText   = "";
    List<MediaFile> pics        = new ArrayList<MediaFile>();
    List<Dir>       dirs        = new ArrayList<Dir>();

    dirpage = new Dir(dirFile, title, description, url, introText, FileName);

    FileFinder fileFinder = new FileFinder(title, description, url, dirs, pics, this, refreshFromXml);

    fileFinder.run();
    // executor.execute(fileFinder);
    // executor.shutdown();

    // try {
    // executor.awaitTermination(9999999L, TimeUnit.SECONDS);
    // } catch (InterruptedException e) {
    // e.printStackTrace();
    // }
  }

  /**  */
  private void setNumColumnButtons()
  {
    ActionListener[] actionListeners = numberOfColumnsComboBox.getActionListeners();

    for (ActionListener actionListener : actionListeners)
    {
      numberOfColumnsComboBox.removeActionListener(actionListener);
    }

    numberOfColumnsComboBox.setSelectedItem(String.valueOf(numColumns));
    numberOfColumnsComboBox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          numberOfColumnsDropdownSelected();
        }
      });
  }

  @Override
  public void getInfoFromXmlFile(boolean shouldSetNumColumns)
  {
    MetaDataReader metadataReader = new MetaDataReader(this, currentDir, metadataFileName, dirpage, shouldSetNumColumns, numColumns, numRows);

    metadataReader.getInfoFromXmlFile();

    boolean defaultDisplayExifDropdown = dirpage.getDefaultDisplayExifDropdown();

    showEXIFInfoInCheckBox.setSelected(defaultDisplayExifDropdown);
    setExifInfoTasks();
  }

  /**  */
  private void setTheCursor(Cursor theCursor)
  {
    setCursor(theCursor);
  }

  /**  */
  private void enableButtons(boolean enabled)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.enableButtons");
    }

    create_smFilesInRadioButton.setEnabled(enabled);
    makeThumbnailsDirectoryRadioButton.setEnabled(enabled);
    saveXMLButton.setEnabled(enabled);
    saveHTMLButton.setEnabled(enabled);
    numberOfColumnsComboBox.setEnabled(enabled);
    numberOfRowsComboBox.setEnabled(enabled);
    addUpIconCheckBox.setEnabled(enabled);
  }
  // ------------------------ INTERFACE METHODS ------------------------

  // --------------------- Interface KeyListener ---------------------
  @Override
  public void keyTyped(KeyEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.keyTyped");
    }

    int keyCode = e.getKeyCode();

    if (logger.isDebugEnabled())
    {
      logger.debug("keyCode = " + keyCode);
    }

    switch (keyCode)
    {
      case VK_X:
        cutAction();
        break;

      case VK_V:
        pasteAction();
        break;

      case VK_DELETE:
        deleteAction();
        break;

      default:
    }
  }

  /**  */
  public void keyPressed(KeyEvent e)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.keyPressed");
    }
  }

  /**  */
  public void keyReleased(KeyEvent e) {}

  // --------------------- Interface MainFrameInterface ---------------------
  @Override
  public File getCurrentDir()
  {
    return currentDir;
  }

  @Override
  public void setStatus(String text)
  {
    statusBar.setText(text);
    statusBar.setVisible(false);
    statusBar.setVisible(true);
  }

  @Override
  public Dir getDirpage()
  {
    return dirpage;
  }

  @Override
  public SortCriteria getSortCriteria()
  {
    if (shouldSortByFileName())
    {
      return FileName;
    }

    if (shouldSortByFileTimestamp())
    {
      return FileTimestamp;
    }

    if (shouldSortByPictureTimestamp())
    {
      return ImageTimestamp;
    }

    return FileName;
  }

  @Override
  public void setAddUpLink(boolean addUpLink)
  {
    addUpIconCheckBox.setSelected(addUpLink);
  }

  @Override
  public void setNumColumnButtons(int number)
  {
    numColumns = number;
    setNumColumnButtons();
  }

  /**  */
  public void setNumRowsDropdown(int numberOfRows)
  {
    numberOfRowsComboBox.setSelectedItem(String.valueOf(numberOfRows));
    numRows = numberOfRows;
  }

  /** Sets the current dir, and reads in any files in it. */
  public void setCurrentDir(File theFile, boolean getNumColumnsFromXml)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.setCurrentDir");
    }

    setCursor(waitCursor);
    saveHTMLButton.setEnabled(true);
    saveXMLButton.setEnabled(true);

    if (!currentDir.equals(theFile))
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("MainFrame.saveMetadata");
      }

      if (isInitialUse)
      {
        isInitialUse = false;
      }
      else
      {
        saveMetadata(currentDir, dirpage, numColumns, numRows, addUpIconCheckBox.isSelected());
      }

      currentDir = theFile;
      settings.setLastVisitedDir(theFile.getAbsolutePath());
      breadCrumbPanel.populateBreadCrumbPanel(currentDir);
      refresh(getNumColumnsFromXml);
    }

    saveSettings();

    // enableButtons(true);
    setCursor(normalCursor);
  }

  @Override
  public PictureScrollPane getPictureScrollPane()
  {
    return pictureScrollPane;
  }

  @Override
  public void stopGlasspane()
  {
    if (useGlassPane)
    {
      glassPane.stop();
    }
  }

  // --------------------------- main() method ---------------------------
  public static void main(String[] args)
  {
    NewMainFrame mainFrame = new NewMainFrame();

    mainFrame.initializeUi();
  }

  // -------------------------- OTHER METHODS --------------------------
  private void arrangeComponents()
  {
    if (isInitialUse)
    {
      picturePanel = new PicPanel(this, new ArrayList<MediaFile>(), numColumns, dirpage);

      // picturePanel = new PicTable(this, new ArrayList<MediaFile>(), numColumns, dirpage);
      dirTable = new DirTable(this, new ArrayList<Dir>());
      breadCrumbPanel.populateBreadCrumbPanel();
    }
    else
    {
      setUpDirPage(true);
      picturePanel = new PicPanel(this, dirpage.getPics(), numColumns, dirpage);

      // picturePanel = new PicTable(this, dirpage.getPics(), numColumns, dirpage);
      dirTable = new DirTable(this, dirpage.getDirs());
      breadCrumbPanel.populateBreadCrumbPanel(currentDir);
    }

    if (logger.isDebugEnabled())
    {
      logger.debug("close 2");
    }
  }

  /**  */
  private void cutAction()
  {
    picturePanel.cutAction();
  }

  /**  */
  private void deleteAction()
  {
    picturePanel.deleteAction();
  }

  /**  */
  private void pasteAction()
  {
    picturePanel.pasteAction();
  }

  /**  */
  private void setSkin(String skinName)
  {
    for (Skin availableSkin : availableSkins)
    {
      if (availableSkin.getSkinName().equalsIgnoreCase(skinName))
      {
        skin = availableSkin;

        break;
      }
    }
  }

  boolean shouldSortByFileName()
  {
    return fileNameRadioButton.isSelected();
  }

  boolean shouldSortByFileTimestamp()
  {
    return pictureDateRadioButton.isSelected();
  }

  boolean shouldSortByPictureTimestamp()
  {
    return pictureDateRadioButton.isSelected();
  }
}
