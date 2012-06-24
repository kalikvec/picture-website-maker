package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import com.nurflugel.picturebrowserservlet.htmlstuff.DefaultSkin;
import com.nurflugel.picturebrowserservlet.htmlstuff.HtmlWriter;
import com.nurflugel.picturebrowserservlet.htmlstuff.Skin;
import com.nurflugel.picturebrowserservlet.settings.Settings;
import org.apache.log4j.Category;
import javax.help.CSH;
import javax.help.HelpSet;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.prefs.Preferences;
import static com.nurflugel.picturebrowserservlet.gui.MetadataWriter.saveMetadata;
import static com.nurflugel.picturebrowserservlet.gui.SortCriteria.*;
import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.metadataFileName;
import static com.nurflugel.picturebrowserservlet.gui.ThumbnailReaderWriterFactory.getThumbnailReaderWriter;
import static com.nurflugel.picturebrowserservlet.util.UtilMethods.getNumRowsFromString;
import static java.awt.BorderLayout.NORTH;
import static java.awt.Cursor.getPredefinedCursor;
import static java.awt.Toolkit.getDefaultToolkit;
import static java.awt.event.KeyEvent.*;
import static java.util.concurrent.Executors.newSingleThreadExecutor;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.JOptionPane.showMessageDialog;

/** @author  Douglas Bullard */
@SuppressWarnings({
                    "MethodWithTooManyParameters", "RefusedBequest", "AssignmentToStaticFieldFromInstanceMethod", "MethodParameterNamingConvention",
                    "MethodOnlyUsedFromInnerClass"
                  })
public class MainFrame extends JFrame implements KeyListener
{
  public static int DEFAULT_NUM_COLUMNS = 4;  // todo in Settings??

  /** Use serialVersionUID for interoperability. */
  private static final long            serialVersionUID                  = 7856355354670324045L;
  private static Category              logger                            = LogFactory.getInstance(MainFrame.class);
  private static ThumbnailReaderWriter thumbnailReaderWriter;
  private boolean                      isInitialUse                      = true;
  private BreadCrumbPanel              breadCrumbPanel;
  private Container                    contentPane;
  private Cursor                       normalCursor                      = getPredefinedCursor(Cursor.DEFAULT_CURSOR);
  private Cursor                       waitCursor                        = getPredefinedCursor(Cursor.WAIT_CURSOR);
  private Toolkit                      toolkit                           = getDefaultToolkit();
  private Dimension                    screenSize                        = toolkit.getScreenSize();
  private Dir                          dirpage;
  private DirTable                     dirTable;
  private File                         currentDir;   // = new File(TagsAndStuff.defaultDirName);
  private HtmlWriter                   htmlWriter;
  private int                          numColumns                        = DEFAULT_NUM_COLUMNS;
  private int                          numRows                           = 4;
  private JButton                      quitButton;
  private JButton                      saveHtmlButton;
  private JButton                      saveXmlButton;
  private JCheckBox                    generatePreviewImagesCheckbox;
  private JCheckBox                    showExifInfoCheckbox;
  private JCheckBox                    upIconCheckbox;
  private JComboBox                    numRowsPerPageDropdown;
  private JComboBox                    numberOfColumnsDropdown;
  private JMenuItem                    aboutMenuItem;
  private JMenuItem                    contentsMenuItem;
  private JMenuItem                    cutMenuItem;  // todo implement this - also key listeners
  private JMenuItem                    defaultSkinMenuItem;
  private JMenuItem                    deleteMenuItem;  // todo implement this - also key listeners
  private JMenuItem                    exitMenuItem;
  private JMenuItem                    pasteMenuItem;  // todo implement this - also key listeners
  private JMenuItem                    renameMenuItem;
  private JMenuItem                    saveHtmlMenuItem;
  private JMenuItem                    saveXmlMenuItem;
  private JMenuItem                    wingSkinMenuItem;
  private JPanel                       mainPanel;
  private JPanel                       mainTopPanel;
  private JRadioButton                 dirsThumbnailsButton;
  private JRadioButton                 forwardsRadioButton;
  private JRadioButton                 reverseRadioButton;
  private JRadioButton                 smThumbnailsButton;
  private JRadioButton                 sortByFileNameRadioButton;
  private JRadioButton                 sortByFileTimestampRadioButton;
  private JRadioButton                 sortByPictureTimestampRadioButton;
  private JCheckBox                    enablePrePostBODYCheckBox;
  private PicContainer                 picturePanel;
  private PictureScrollPane            pictureScrollPane;
  private Preferences                  preferences;
  private Skin[]                       availableSkins;
  private Skin                         skin;
  private StatusBar                    statusBar;
  private boolean                      useGlassPane                      = true;
  private InfiniteProgressPanel        glassPane;
  private transient ExecutorService    executor;
  private ProgressMonitor              progressMonitor;
  private final Settings               settings;

  // static Settings getSettings()
  // {
  // return SettingsReader.read();
  // }
  /**  */
  public static ThumbnailReaderWriter getThumbnailHandler()
  {
    return thumbnailReaderWriter;
  }
  // todos - add JS script section for pics

  /** Creates a new MainFrame object. */
  public MainFrame()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.MainFrame");
    }

    executor   = newSingleThreadExecutor();
    settings   = new Settings();
    currentDir = new File(settings.getLastVisitedDir());

    // EventQueue.invokeLater(new Runnable()
    // {
    skin = new DefaultSkin();
    initComponents();

    // timer = new Timer(ONE_SECOND, new TimerListener());
    boolean selected = dirsThumbnailsButton.isSelected();

    thumbnailReaderWriter = getThumbnailReaderWriter(selected);
    arrangeComponents();
    addListeners();
    setInitialSettings();
    htmlWriter = new HtmlWriter(this);
  }

  /**  */
  @SuppressWarnings({ "OverlyLongMethod" })
  private void initComponents()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.initComponents");
    }

    mainPanel = new JPanel();

    JMenuBar mainMenuBar = new JMenuBar();
    JMenu    fileMenu    = new JMenu("File");

    saveHtmlMenuItem = new JMenuItem("Save HTML pages");
    saveXmlMenuItem  = new JMenuItem("Save XML metatdata");
    exitMenuItem     = new JMenuItem("Exit");

    JMenu editMenu   = new JMenu("Edit");

    cutMenuItem    = new JMenuItem("Cut", VK_X);
    pasteMenuItem  = new JMenuItem("Paste", VK_V);
    deleteMenuItem = new JMenuItem("Delete", VK_DELETE);
    renameMenuItem = new JMenuItem("Rename", VK_R);

    JMenu skinsMenu = new JMenu("Skins");

    skinsMenu.setEnabled(false);
    defaultSkinMenuItem = new JMenuItem("Default");
    wingSkinMenuItem    = new JMenuItem("Wing");

    JMenu helpMenu      = new JMenu("Help");

    contentsMenuItem = new JMenuItem("Contents");
    aboutMenuItem    = new JMenuItem("About");
    numberOfColumnsDropdown.setModel(new JComboBox(new String[] { "Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10" }).getModel());
    numRowsPerPageDropdown.setModel(new JComboBox(new String[] { "Select", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Unlimited" })
                                      .getModel());

    // upIconCheckbox       = new JCheckBox("Add 'Up' icon to page", true);
    // showExifInfoCheckbox = new JCheckBox("Show EXIF dropdown info", true);
    fileMenu.setText("File");
    saveHtmlMenuItem.setEnabled(false);
    saveXmlMenuItem.setEnabled(false);
    fileMenu.add(saveHtmlMenuItem);
    fileMenu.add(saveXmlMenuItem);
    fileMenu.add(exitMenuItem);
    editMenu.add(cutMenuItem);
    editMenu.add(pasteMenuItem);
    editMenu.add(deleteMenuItem);
    editMenu.add(deleteMenuItem);
    skinsMenu.add(defaultSkinMenuItem);
    skinsMenu.add(wingSkinMenuItem);
    helpMenu.add(contentsMenuItem);
    helpMenu.add(aboutMenuItem);
    mainMenuBar.add(fileMenu);
    mainMenuBar.add(editMenu);
    mainMenuBar.add(skinsMenu);
    mainMenuBar.add(helpMenu);
    setJMenuBar(mainMenuBar);
    breadCrumbPanel   = new BreadCrumbPanel(this);
    statusBar         = new StatusBar();
    contentPane       = getContentPane();
    pictureScrollPane = new PictureScrollPane();
    enableButtons(false);
    setTitle("Picture Browser Servlet Front End");
    glassPane = new InfiniteProgressPanel("Converting your files, please wait");
    setGlassPane(glassPane);
  }

  /**  */
  private void enableButtons(boolean enabled)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.enableButtons");
    }

    smThumbnailsButton.setEnabled(enabled);
    dirsThumbnailsButton.setEnabled(enabled);
    saveXmlButton.setEnabled(enabled);
    saveHtmlButton.setEnabled(enabled);
    numberOfColumnsDropdown.setEnabled(enabled);
    numRowsPerPageDropdown.setEnabled(enabled);
    upIconCheckbox.setEnabled(enabled);
  }

  /**  */
  private void arrangeComponents()
  {
    LayoutManager mainPanelLayout = new BoxLayout(mainPanel, Y_AXIS);

    mainPanel.setLayout(mainPanelLayout);

    // progressMonitor = new ProgressMonitor(this, "Loading images...", null, 0, 0);
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

    setNumColumnButtons();
    contentPane.add(mainTopPanel, NORTH);
    mainPanel.setBorder(new EtchedBorder());
    pictureScrollPane.setViewportView((Component) picturePanel);

    // pictureScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER );
    mainPanel.add(breadCrumbPanel);
    mainPanel.add(dirTable);
    mainPanel.add(pictureScrollPane);
    mainPanel.add(statusBar);
    contentPane.add(mainPanel);
    setSize(new Dimension((int) (screenSize.getWidth() / 2), (int) (screenSize.getHeight() / 2)));
    setUpDirPage(true);

    // refresh(true);
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
    ActionListener[] actionListeners = numberOfColumnsDropdown.getActionListeners();

    for (ActionListener actionListener : actionListeners)
    {
      numberOfColumnsDropdown.removeActionListener(actionListener);
    }

    numberOfColumnsDropdown.setSelectedItem(String.valueOf(numColumns));
    numberOfColumnsDropdown.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          numberOfColumnsDropdownSelected();
        }
      });
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
    saveHtmlButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          saveHtml();
        }
      });
    saveXmlButton.addActionListener(new ActionListener()
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
    smThumbnailsButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          thumbnailReaderWriter = getThumbnailReaderWriter(dirsThumbnailsButton.isSelected());
        }
      });
    dirsThumbnailsButton.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          thumbnailReaderWriter = getThumbnailReaderWriter(dirsThumbnailsButton.isSelected());
        }
      });
    showExifInfoCheckbox.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          setExifInfoTasks();
        }
      });
    exitMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          exitForm();
        }
      });
    numRowsPerPageDropdown.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          numRows = getNumRowsFromDropdown();
        }
      });
    numberOfColumnsDropdown.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          numberOfColumnsDropdownSelected();
        }
      });
    aboutMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          showAboutDialog();
        }
      });
    saveHtmlMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          saveHtml();
        }
      });
    saveXmlMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          saveMetaData();
        }
      });
    defaultSkinMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          setSkin("default");
        }
      });
    wingSkinMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          setSkin("wing");
        }
      });
    cutMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          cutAction();
        }
      });
    pasteMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          pasteAction();
        }
      });
    deleteMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          deleteAction();
        }
      });
    renameMenuItem.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent evt)
        {
          showHelpStuff();  // todo wtf???
        }
      });

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

      contentsMenuItem.addActionListener(displayHelpFromSource);
    }
    catch (Exception e)
    {
      logger.error("error", e);
    }
  }

  private void handlePrePostBodyCheck()
  {
    boolean selected = enablePrePostBODYCheckBox.isSelected();

    if (selected)
    {
      // show pre/post text box
      PrePostDialog dialog = new PrePostDialog();

      if (!dialog.wasCanceled())
      {
        String preBodyText  = dialog.getPreBodyText();
        String postBodyText = dialog.getPostBodyText();

        saveSettings();
      }
    }
    else
    {
      saveSettings();
    }
  }

  void saveSettings()
  {                                                        // add setting settings in listeners...

    Settings settings     = new Settings();
    File     currentDir1  = getCurrentDir();
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
  public boolean showExifInfoCheckBox()
  {
    return showExifInfoCheckbox.isSelected();
  }

  /**  */
  private void setExifInfoTasks()
  {
    boolean selected = showExifInfoCheckbox.isSelected();

    setExifUiSettings(selected);
  }

  private void setExifUiSettings(boolean selected)
  {
    dirpage.setDisplayExifDropdown(selected);
    picturePanel.showExifDropdowns(selected);
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
      saveMetadata(currentDir, dirpage, numColumns, numRows, upIconCheckbox.isSelected());
    }

    saveSettings();
    System.exit(0);
  }

  /**  */
  private int getNumRowsFromDropdown()
  {
    return getNumRowsFromString(getNumRowsPerPageFromDropdown());
  }

  /**  */
  public String getNumRowsPerPageFromDropdown()
  {
    Object selectedItem = numRowsPerPageDropdown.getSelectedItem();

    return (String) selectedItem;
  }

  /**  */
  private void numberOfColumnsDropdownSelected()
  {
    numColumns = getNumColumnsFromDropdown();
    refresh(false);
  }

  /**  */
  private int getNumColumnsFromDropdown()
  {
    return getNumRowsFromString(getNumColumnsPerPageFromDropdown());
  }

  /**  */
  private String getNumColumnsPerPageFromDropdown()
  {
    Object selectedItem = numberOfColumnsDropdown.getSelectedItem();

    return (String) selectedItem;
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

  /**  */
  void getInfoFromXmlFile(boolean shouldSetNumColumns)
  {
    MetaDataReader metadataReader = new MetaDataReader(this, currentDir, metadataFileName, dirpage, shouldSetNumColumns, numColumns, numRows);

    metadataReader.getInfoFromXmlFile();

    boolean defaultDisplayExifDropdown = dirpage.getDefaultDisplayExifDropdown();

    showExifInfoCheckbox.setSelected(defaultDisplayExifDropdown);
    setExifInfoTasks();
  }

  public boolean shouldWritePreviews()
  {
    return generatePreviewImagesCheckbox.isSelected();
  }

  /**  */
  private void setTheCursor(Cursor theCursor)
  {
    setCursor(theCursor);
  }

  /**  */
  private void showAboutDialog()
  {
    showMessageDialog(this, "Nurflugel PictureBrowser by Douglas Bullard\nSend comments to: dbullard@nurflugel.com");
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
    saveMetadata(currentDir, dirpage, numColumns, numRows, upIconCheckbox.isSelected());
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

  /**  */
  private void cutAction()
  {
    picturePanel.cutAction();
  }

  /**  */
  private void pasteAction()
  {
    picturePanel.pasteAction();
  }

  /**  */
  private void deleteAction()
  {
    picturePanel.deleteAction();
  }

  /**  */
  private void showHelpStuff()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.showHelpStuff");
    }

    String text = "This program allows you to easily create web pages for all those digital photos, movies, and mp3s.  Photoshop does a nice\n"
                    + "job of creating a web site from your pictures, but you can't add captions, and if you edit the html\n"
                    + " manually, then you're screwed if you add more images to the same directory.\n\n"               //
                    + "When the program opens, click the \"Browse\" button to point\n"                                 //
                    + " to a directory you want to make a web page of.\n\n"                                            //
                    + "You can re-arrange the order by dragging and dropping the pictures,\n"                          //
                    + " and you can rename or delete them by right-clicking\n"                                         //
                    + "the icons.\n\n"                                                                                 //
                    + "Simply clicking on a icon will open the icon.  If it's a picture,\n"                            //
                    + " it'll open in this application.  If it's \n"                                                   //
                    + "another type of file, it'll open the file with your system's default\n"                         //
                    + " tool for that file type. (OK, this only works on \n"                                           //
                    + "WinDoze for now, but hey, it's free, right?)\n\n"                                               //
                    + "For any file (or directory) you can store a comment in the area next\n"                         //
                    + " to the file.  This will be displayed under the picture on the web page.\n\n"                   //
                    + "Pictures from a digital source will have a dropdown with any EXIF data displayed as well.\n\n"  //
                    + "Clicking on the \"Save HTML\" button saves the generated HTML to a file.\n"                     //
                    + "  If there are more rows than allowed by the rows dropdown control, \n"                         //
                    + "there will be several pages generated.  The first file is always \n"                            //
                    + "\"index.html\", the scond \"index1.html\", etc.\n\n"                                            //
                    + "The metadata (descriptions, display order, etc) are stored in each \n"                          //
                    + "directory with the name \"metadata.xml\" automatically, but \n"                                 //
                    + "you can save it at any time by clicking on the \"Save Metatdata\" button.\n\n"                  //
                    + "To move around, you can use the Browse button, click on a directory, \n"                        //
                    + "or click on the text in the middle of the screen with the name of the \n"                       //
                    + "current directory.  You can click on any level of this text, and it will go\n"                  //
                    + " to that directory.  For example, if the text says 'c:\\temp\\pics\\vacation',\n"               //
                    + "and you click on the 'pics' part of that, you'll go to the 'pics' directory.\n\n"               //
                    + "To look at the web page you created, open an Explorer window and \n"                            //
                    + "click on the file called 'index.html'.  That's it!\n\n"                                         //
                    + "You can copy the entire directory to a web server, and your web page is up and running!.\n\n"   //
                    + "Have fun, and let me know what you think of the program!\n\n"                                   //
                    + "Doug";

    showMessageDialog(this, text);
  }

  private void setInitialSettings()
  {
    skin = (skin == null) ? new DefaultSkin()
                          : skin;
    setNumColumnButtons(settings.getNumColumns());
    setNumRowsDropdown(settings.getNumRows());
    setExifUiSettings(settings.isShowExifInfo());
    generatePreviewImagesCheckbox.setSelected(settings.isShowPreviews());
  }

  /**  */
  void setNumColumnButtons(int number)
  {
    numColumns = number;
    setNumColumnButtons();
  }

  /**  */
  public void setNumRowsDropdown(int numberOfRows)
  {
    numRowsPerPageDropdown.setSelectedItem(String.valueOf(numberOfRows));
    numRows = numberOfRows;
  }
  // ------------------------ INTERFACE METHODS ------------------------

  // --------------------- Interface KeyListener ---------------------
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
  // --------------------------- main() method ---------------------------

  /**  */
  public static void main(String[] args)
  {
    JFrame.setDefaultLookAndFeelDecorated(false);
    new MainFrame().setVisible(true);
  }
  // -------------------------- OTHER METHODS --------------------------

  // ------------------------ Class Methods ------------------------
  /**  */
  public void clearStatusBar()
  {
    statusBar.clear();
  }

  SortCriteria getSortCriteria()
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

  boolean shouldSortByFileName()
  {
    return sortByFileNameRadioButton.isSelected();
  }

  boolean shouldSortByFileTimestamp()
  {
    return sortByFileTimestampRadioButton.isSelected();
  }

  boolean shouldSortByPictureTimestamp()
  {
    return sortByPictureTimestampRadioButton.isSelected();
  }

  /**  */
  private void renameAction()
  {
    picturePanel.renameAction();
  }

  /**  */
  public void setAddUpLink(boolean addUpLink)
  {
    upIconCheckbox.setSelected(addUpLink);
  }

  /** Sets the current dir, and reads in any files in it. */
  void setCurrentDir(File theFile, boolean getNumColumnsFromXml)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("MainFrame.setCurrentDir");
    }

    setCursor(waitCursor);
    saveHtmlMenuItem.setEnabled(true);
    saveXmlMenuItem.setEnabled(true);

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
        saveMetadata(currentDir, dirpage, numColumns, numRows, upIconCheckbox.isSelected());
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

  /**  */
  void setStatus(String text)
  {
    statusBar.setText(text);
    statusBar.setVisible(false);
    statusBar.setVisible(true);
  }

  /**  */
  public boolean shouldAddUpLink()
  {
    return upIconCheckbox.isSelected();
  }

  void stopGlasspane()
  {
    if (useGlassPane)
    {
      glassPane.stop();
    }
  }
  // --------------------- GETTER / SETTER METHODS ---------------------

  // ------------------------ GETTER/SETTER METHODS ------------------------
  /**  */
  public File getCurrentDir()
  {
    return currentDir;
  }

  /**  */
  public Dir getDirpage()
  {
    return dirpage;
  }

  /**  */
  public int getNumColumns()
  {
    return numColumns;
  }

  public PictureScrollPane getPictureScrollPane()
  {
    return pictureScrollPane;
  }

  /**  */
  public Skin getSkin()
  {
    return skin;
  }

  public Settings getSettings()
  {
    return settings;
  }
}
