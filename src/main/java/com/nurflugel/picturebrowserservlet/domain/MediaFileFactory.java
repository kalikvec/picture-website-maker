/**
 * Created by IntelliJ IDEA. User: Douglas Bullard Date: Jun 22, 2003 Time: 4:06:35 PM To change this template use Options | File Templates.
 */
package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.util.UtilMethods;
import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import java.io.File;

public class MediaFileFactory
{
  private MediaFileFactory() {}

  public static MediaFile getMediaFile(String lowerCaseFileName, File file, String title, String description, String url, boolean displayExifDropdown,
                                       SortCriteria sortCriteria)
  {
    MediaFile mediaFile = null;

    if (lowerCaseFileName.endsWith(".jpg") || lowerCaseFileName.endsWith(".jpeg"))
    {
      mediaFile = new Jpg(file, title, description, url, displayExifDropdown, sortCriteria);
    }

    if (lowerCaseFileName.endsWith(".gif"))
    {
      mediaFile = new Gif(file, title, description, url, sortCriteria);
    }
    else if (UtilMethods.isAnimation(lowerCaseFileName))
    {
      mediaFile = new Mpeg(file, title, description, url, sortCriteria);
    }
    else if (lowerCaseFileName.endsWith(".avi"))
    {
      mediaFile = new Avi(file, title, description, url, sortCriteria);
    }
    else if (lowerCaseFileName.endsWith(".wav"))
    {
      mediaFile = new Wav(file, title, description, url, sortCriteria);
    }
    else if (lowerCaseFileName.endsWith(".mp3"))
    {
      mediaFile = new Mp3(file, title, description, url, sortCriteria);
    }

    return mediaFile;
  }
}
