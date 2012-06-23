package com.nurflugel.picturebrowserservlet.settings;

import static com.nurflugel.picturebrowserservlet.gui.TagsAndStuff.*;
import java.util.prefs.Preferences;

public class SettingsWriter
{
  private SettingsWriter() {}

  // ------------------------ OTHER METHODS ------------------------
  public static void save(Settings settings)
  {
    Preferences preferences = Preferences.userNodeForPackage(SettingsWriter.class);

    preferences.put(LAST_VISITED_DIR, settings.getLastVisitedDir());
    preferences.putInt(NUM_COLS, settings.getNumColumns());
    preferences.putInt(NUM_ROWS, settings.getNumRows());
    preferences.putBoolean(SHOW_EXIF_INFO, settings.isShowExifInfo());
    preferences.putBoolean(SHOW_PREVIEWS, settings.isShowPreviews());
  }
}
