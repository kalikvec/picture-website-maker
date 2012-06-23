package com.nurflugel.picturebrowserservlet.settings;

import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.*;
import java.util.prefs.Preferences;

public class SettingsReader
{
  private SettingsReader() {}

  /**  */
  public static Settings read()
  {
    Settings    settings    = new Settings();
    Preferences preferences = Preferences.userNodeForPackage(SettingsReader.class);

    settings.setLastVisitedDir(preferences.get(LAST_VISITED_DIR, "."));
    settings.setNumColumns(preferences.getInt(NUM_COLS, 4));
    settings.setNumRows(preferences.getInt(NUM_ROWS, 4));
    settings.setShowExifInfo(preferences.getBoolean(SHOW_EXIF_INFO, true));
    settings.setShowPreviews(preferences.getBoolean(SHOW_PREVIEWS, true));

    return settings;
  }
}
