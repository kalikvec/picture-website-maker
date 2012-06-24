package com.nurflugel.picturebrowserservlet.settings;

import java.util.Properties;
import java.util.prefs.Preferences;

/**  */
public class Settings
{
  public static final String  LAST_VISITED_DIR       = "LAST_VISITED_DIR";
  public static final String  NUM_COLS               = "NUM_COLS";
  public static final String  NUM_ROWS               = "NUM_ROWS";
  public static final String  SHOW_EXIF_INFO         = "SHOW_EXIF_INFO";
  public static final String  SHOW_PREVIEWS          = "SHOW_PREVIEWS";
  private static final String USE_PRE_POST_BODY_TEXT = "usePrePostBodyText";
  private static final String PRE_BODY_TEXT          = "preBodyText";
  private static final String POST_BODY_TEXT         = "postBodyText";
  private String              lastVisitedDir;
  private boolean             showExifInfo;
  private boolean             showPreviews;
  private int                 numColumns;
  private int                 numRows;
  private boolean             usePrePostBodyText;
  private String              preBodyText;
  private String              postBodyText;
  private final Preferences   preferences;

  public Settings()
  {
    preferences = Preferences.userNodeForPackage(Settings.class);

    String userHomeDir = System.getProperty("user.home");

    lastVisitedDir     = preferences.get(LAST_VISITED_DIR, userHomeDir);
    numColumns         = preferences.getInt(NUM_COLS, 4);
    numRows            = preferences.getInt(NUM_ROWS, 4);
    showExifInfo       = preferences.getBoolean(SHOW_EXIF_INFO, true);
    showPreviews       = preferences.getBoolean(SHOW_PREVIEWS, true);
    usePrePostBodyText = preferences.getBoolean(USE_PRE_POST_BODY_TEXT, false);
    preBodyText        = preferences.get(PRE_BODY_TEXT, "");
    postBodyText       = preferences.get(POST_BODY_TEXT, "");
  }

  // -------------------------- OTHER METHODS --------------------------
  public void save()
  {
    preferences.put(LAST_VISITED_DIR, lastVisitedDir);
    preferences.putInt(NUM_COLS, numColumns);
    preferences.putInt(NUM_ROWS, numRows);
    preferences.putBoolean(SHOW_EXIF_INFO, showExifInfo);
    preferences.putBoolean(SHOW_PREVIEWS, showPreviews);
    preferences.putBoolean(USE_PRE_POST_BODY_TEXT, usePrePostBodyText);
    preferences.put(PRE_BODY_TEXT, preBodyText);
    preferences.put(POST_BODY_TEXT, postBodyText);
  }

  // --------------------- GETTER / SETTER METHODS ---------------------
  public String getLastVisitedDir()
  {
    return lastVisitedDir;
  }

  public void setLastVisitedDir(String lastVisitedDir)
  {
    this.lastVisitedDir = lastVisitedDir;
    save();
  }

  public int getNumColumns()
  {
    return numColumns;
  }

  public void setNumColumns(int numColumns)
  {
    this.numColumns = numColumns;
    save();
  }

  public int getNumRows()
  {
    return numRows;
  }

  public void setNumRows(int numRows)
  {
    this.numRows = numRows;
    save();
  }

  public String getPostBodyText()
  {
    return postBodyText;
  }

  public void setPostBodyText(String postBodyText)
  {
    this.postBodyText = postBodyText;
    save();
  }

  public String getPreBodyText()
  {
    return preBodyText;
  }

  public void setPreBodyText(String preBodyText)
  {
    this.preBodyText = preBodyText;
    save();
  }

  public boolean isShowExifInfo()
  {
    return showExifInfo;
  }

  public void setShowExifInfo(boolean showExifInfo)
  {
    this.showExifInfo = showExifInfo;
    save();
  }

  public boolean isShowPreviews()
  {
    return showPreviews;
  }

  public void setShowPreviews(boolean showPreviews)
  {
    this.showPreviews = showPreviews;
    save();
  }
}
