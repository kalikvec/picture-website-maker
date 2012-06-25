package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import org.apache.log4j.Logger;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import java.util.List;

class PicPanel extends JPanel implements PicContainer
{
  private static Logger            logger            = LogFactory.getInstance(PicPanel.class);
  private MainFrameInterface       theFrame;
  private List<MediaFile>          fileList          = new ArrayList<MediaFile>();
  private int                      numColumns        = 2;
  private Map<MediaFile, PicLabel> picMap            = new HashMap<MediaFile, PicLabel>();
  private int                      maxWidth;
  private int                      maxHeight;
  private boolean                  isDone            = true;
  private int                      loaderIndex;
  private List<PicLabel>           selectedItems     = new ArrayList<PicLabel>();
  private Dir                      theDir;
  private boolean                  showExifDropdowns = true;

  /** Creates a new PicPanel object. */
  PicPanel(MainFrameInterface theFrame, List<MediaFile> labelList, int numColumns, Dir dir)
  {
    this.theFrame = theFrame;
    theDir        = dir;
    maxWidth      = theFrame.getWidth() - theFrame.getInsets().left - theFrame.getInsets().right;
    maxHeight     = getSize().height;
    refresh(labelList, numColumns, theDir);
  }

  /** need the progress monitor reference to detect clicks on the "cancel" button. */
  public void refresh(List<MediaFile> newPicList, int numColumns, Dir dir)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.refresh");
    }

    theDir = dir;
    isDone = false;

    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.construct");
    }

    doTheRefresh(newPicList, numColumns);
  }

  /**  */
  private void doTheRefresh(List<MediaFile> newPicList, int numColumns)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.doTheRefresh");
    }

    int newPicListSize = newPicList.size();

    fileList.clear();
    fileList.addAll(newPicList);
    this.numColumns = numColumns;
    removeAll();

    int           numRows = (newPicListSize / numColumns) + 1;
    LayoutManager layout  = new GridLayout(numRows, 1);

    if (logger.isDebugEnabled())
    {
      logger.debug("=======>Layout set to new grid layout, " + numRows + " rows, " + numColumns + " columns");
    }

    setLayout(layout);
    Collections.sort(fileList);

    for (int i = 0; i < newPicListSize; i++)
    {
      addPicLabel(i);
    }

    if (logger.isDebugEnabled())
    {
      logger.debug("close 4");
    }

    validate();
    isDone = true;
  }

  private void addPicLabel(int i)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.refresh -          loading: " + i);
    }

    loaderIndex = i;

    PicLabel  label;
    MediaFile pic = fileList.get(i);

    if (picMap.containsKey(pic))
    {
      label = picMap.get(pic);

      if (logger.isDebugEnabled())
      {
        logger.debug("PicPanel.refresh - got label from map");
      }
    }
    else
    {
      label = new PicLabel(pic, theFrame, this);
      picMap.put(pic, label);

      if (logger.isDebugEnabled())
      {
        logger.debug("PicPanel.refresh - putting label into map");
      }
    }

    int maxHeight = label.getSize().height;

    label.setSize(maxWidth, maxHeight);

    boolean defaultDisplayExifDropdown = true;

    if (theDir != null)
    {
      defaultDisplayExifDropdown = theDir.getDefaultDisplayExifDropdown();
    }

    if (defaultDisplayExifDropdown)
    {
      boolean displayExifDropdown = pic.shouldDisplayExifDropdown();

      if (logger.isDebugEnabled())
      {
        logger.debug("The pic " + pic.getFile().getName() + " has displaydropdown of " + displayExifDropdown);
      }

      label.setShowExifDropdown(displayExifDropdown);
    }
    else
    {
      label.setShowExifDropdown(false);
    }

    Dimension dimension = new Dimension(maxWidth, maxHeight);

    if (logger.isDebugEnabled())
    {
      logger.debug("dimension = " + dimension);
    }

    add(label);
    label.raiseBorder();
  }

  // ------------------------ OTHER METHODS ------------------------
  public void cutAction()
  {
    Component[] components = getComponents();
    Dir         dirpage    = theFrame.getDirpage();

    selectedItems.clear();

    for (Component component1 : components)
    {
      PicLabel picLabel = (PicLabel) component1;

      if (picLabel.isSelected())
      {
        selectedItems.add(picLabel);
        dirpage.remove(picLabel.getThePic());
      }
    }

    List<MediaFile> mediaFiles = new ArrayList<MediaFile>(fileList);

    refresh(mediaFiles, numColumns, theDir);
  }

  /**  */
  public void deleteAction()
  {
    List<PicLabel> selectedPics = getSelectedPics();

    for (PicLabel picLabel : selectedPics)
    {
      picLabel.delete();
    }
  }

  /**  */
  public boolean isDone()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.isDone " + isDone);
    }

    return isDone;
  }

  /**  */
  public void movePic(String fileName, String targetFileName)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.movePic");
    }

    PicLabel  fileToMove          = findMediaFile(fileName);
    int       indexToPlaceAheadOf = getIndexToPlaceAheadOf(targetFileName);
    MediaFile thePic              = fileToMove.getThePic();

    fileList.remove(thePic);
    fileList.add(indexToPlaceAheadOf, thePic);
    renumberList();

    List<MediaFile> mediaFiles = new ArrayList<MediaFile>(fileList);

    refresh(mediaFiles, numColumns, theDir);
  }

  /**  */
  private PicLabel findMediaFile(String fileName)
  {
    PicLabel    matchingFile = null;
    Component[] components   = getComponents();

    for (Component component : components)
    {
      if (component instanceof PicLabel)
      {
        PicLabel picLabel         = (PicLabel) component;
        String   piclabelFileName = picLabel.getThePic().getFile().getName();

        if (piclabelFileName.equals(fileName))
        {
          matchingFile = picLabel;

          break;
        }
      }
    }

    return matchingFile;
  }

  /**  */
  private int getIndexToPlaceAheadOf(String fileName)
  {
    int         index      = 0;
    Component[] components = getComponents();

    for (Component component : components)
    {
      if (component instanceof PicLabel)
      {
        PicLabel picLabel         = (PicLabel) component;
        String   piclabelFileName = picLabel.getThePic().getFile().getName();

        if (piclabelFileName.equals(fileName))
        {
          break;
        }
      }

      index++;
    }

    return index;
  }

  /** There should only be one pic selected - if there's more, just take the first. If none are selected, do nothing */
  public void pasteAction()
  {
    List<PicLabel> selectedPics = getSelectedPics();

    if (selectedPics.size() > 1)
    {
      PicLabel selectedPic = selectedPics.get(0);

      pasteAction(selectedPic);
    }
  }

  /**  */
  private List<PicLabel> getSelectedPics()
  {
    Component[]    components = getComponents();
    List<PicLabel> theList    = new ArrayList<PicLabel>();

    for (Component component : components)
    {
      PicLabel testPicLabel = (PicLabel) component;

      if (testPicLabel.isSelected())
      {
        theList.add(testPicLabel);
      }
    }

    return new ArrayList<PicLabel>(theList);
  }

  /**  */
  public void pasteAction(PicLabel picLabel)
  {
    Component[] components = getComponents();

    for (int i = 0; i < components.length; i++)
    {
      Component component = components[i];

      if (component.equals(picLabel))
      {
        int picsSize = selectedItems.size();

        for (int j = picsSize - 1; j >= 0; j--)
        {
          fileList.add(i, selectedItems.get(j).getThePic());
        }

        renumberList();

        break;
      }
    }

    List<MediaFile> mediaFiles = new ArrayList<MediaFile>(fileList);

    refresh(mediaFiles, numColumns, theDir);
  }

  /**  */
  public void removeFile(PicLabel theLabel)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.removeFile");
    }

    remove(theLabel);
    fileList.remove(theLabel.getThePic());
    picMap.remove(theLabel.getThePic());
    renumberList();

    List<MediaFile> mediaFiles = new ArrayList<MediaFile>(fileList);

    refresh(mediaFiles, numColumns, theDir);
  }

  /**  */
  private void renumberList()
  {
    int index = 0;

    for (MediaFile mediaFile : fileList)
    {
      mediaFile.setDisplaySequenceNumber(index++);

      if (logger.isDebugEnabled())
      {
        logger.debug("File=" + mediaFile.getFile().getName() + ", position=" + mediaFile.getDisplaySequenceNumber());
      }
    }
  }

  /**  */
  public void renameAction()
  {
    List<PicLabel> selectedPics = getSelectedPics();

    for (PicLabel picLabel : selectedPics)
    {
      picLabel.rename();
    }
  }

  /**  */
  public void reportPosition(double y, PicLabel picLabel)
  {
    int               row                = getRowOfPic(picLabel);
    PictureScrollPane pictureScrollPane  = theFrame.getPictureScrollPane();
    JViewport         viewport           = pictureScrollPane.getViewport();
    Point             viewPosition       = viewport.getViewPosition();
    int               distanceFromTop    = getDistanceFromTop(y, row, viewPosition);
    int               distanceFromBottom = (int) viewport.getSize().getHeight() - distanceFromTop;  // getDistanceFromBottom(y, row,viewPosition);

    if (logger.isDebugEnabled())
    {
      logger.debug("distanceFromTop = " + distanceFromTop);
      logger.debug("distanceFromBottom = " + distanceFromBottom);
    }

    if (distanceFromTop < 10)
    {
      pictureScrollPane.scrollUp();
    }
    else if (distanceFromBottom < 10)
    {
      pictureScrollPane.scrollDown();
    }
  }

  /**  */
  private int getRowOfPic(PicLabel picLabel)
  {
    int         row        = 0;
    Component[] components = getComponents();

    for (int i = 0; i < components.length; i++)
    {
      Component component = components[i];

      if (component.equals(picLabel))
      {
        row = i / numColumns;

        break;
      }
    }

    return row;
  }

  /**  */
  private int getDistanceFromTop(double picLabelMouseEventY, int row, Point viewPosition)
  {
    double      cellHeight             = 0d;
    double      viewportPositionHeight = viewPosition.getY();
    Component[] components             = getComponents();

    if (components.length > 0)
    {
      cellHeight = components[0].getSize().getHeight();
    }

    int distanceFromTop = (int) (picLabelMouseEventY + (((double) row * cellHeight) - viewportPositionHeight));

    return distanceFromTop;
  }

  /**  */
  public void showExifDropdowns(boolean selected)
  {
    Collection<PicLabel> collection = picMap.values();

    for (PicLabel picLabel : collection)
    {
      picLabel.setShowExifDropdown(selected);
    }
  }

  /**  */
  void stop()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.stop");
    }

    loaderIndex = fileList.size();
  }

  /**  */
  public void unselectAllOtherPics(PicLabel picLabel)
  {
    Component[] components = getComponents();

    for (Component component : components)
    {
      if (!component.equals(picLabel))
      {
        PicLabel testPicLabel = (PicLabel) component;

        testPicLabel.setSelected(false);
      }
    }
  }

  // ------------------------ GETTER/SETTER METHODS ------------------------
  public int getLoaderIndex()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("PicPanel.getLoaderIndex loaderIndex=" + loaderIndex);
    }

    return loaderIndex;
  }
}
