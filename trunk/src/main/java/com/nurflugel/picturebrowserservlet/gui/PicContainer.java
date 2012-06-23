package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import java.util.List;

/**
 * Created by IntelliJ IDEA. User: douglasbullard Date: May 10, 2007 Time: 4:08:08 PM To change this template use File | Settings | File Templates.
 */
public interface PicContainer
{
  void showExifDropdowns(boolean selected);
  void cutAction();
  void deleteAction();
  void pasteAction();
  void renameAction();
  void refresh(List<MediaFile> pics, int numColumns, Dir dirpage);
}
