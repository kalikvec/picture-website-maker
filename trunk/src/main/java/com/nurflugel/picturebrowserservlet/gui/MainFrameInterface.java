package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.domain.Dir;
import java.awt.*;
import java.io.File;

/** Created with IntelliJ IDEA. User: douglas_bullard Date: 6/24/12 Time: 17:58 To change this template use File | Settings | File Templates. */
public interface MainFrameInterface
{
  File getCurrentDir();
  void setStatus(String status);
  Dir getDirpage();
  void getInfoFromXmlFile(boolean b);
  SortCriteria getSortCriteria();
  void setAddUpLink(boolean addUpLink);
  void setNumColumnButtons(int numColumns);
  void setNumRowsDropdown(int numRows);
  int getWidth();
  void setCurrentDir(File newDir, boolean getNumColumnsFromXml);
  Insets getInsets();
  PictureScrollPane getPictureScrollPane();
  boolean showExifInfoCheckBox();
  boolean shouldWritePreviews();
  void stopGlasspane();
}
