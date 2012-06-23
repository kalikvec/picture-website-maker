package com.nurflugel.picturebrowserservlet.settings;

/**  */
public class Settings
{
  private String  lastVisitedDir;
  private boolean showExifInfo;
  private boolean showPreviews;
  private int     numColumns;
  private int     numRows;

  public String getLastVisitedDir()
  {
    return lastVisitedDir;
  }

  public void setLastVisitedDir(String lastVisitedDir)
  {
    this.lastVisitedDir = lastVisitedDir;
  }

  public int getNumColumns()
  {
    return numColumns;
  }

  public void setNumColumns(int numColumns)
  {
    this.numColumns = numColumns;
  }

  public int getNumRows()
  {
    return numRows;
  }

  public void setNumRows(int numRows)
  {
    this.numRows = numRows;
  }

  public boolean isShowExifInfo()
  {
    return showExifInfo;
  }

  public void setShowExifInfo(boolean showExifInfo)
  {
    this.showExifInfo = showExifInfo;
  }

  public boolean isShowPreviews()
  {
    return showPreviews;
  }

  public void setShowPreviews(boolean showPreviews)
  {
    this.showPreviews = showPreviews;
  }
}
