package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import com.nurflugel.picturebrowserservlet.util.UtilMethods;
import com.nurflugel.picturebrowserservlet.domain.Dir;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import com.nurflugel.picturebrowserservlet.domain.MediaFileFactory;
import org.apache.log4j.Category;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static com.nurflugel.picturebrowserservlet.domain.MediaFileFactory.getMediaFile;

/** helper class for long-running task. */
@SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
public class FileFinder implements Runnable
{
  private String                title;
  private String                description;
  private String                url;
  private List<Dir>             dirs;
  private List<MediaFile>       pics;
  private static final Category logger         = LogFactory.getInstance(FileFinder.class);
  private MainFrameInterface    mainFrame;
  private boolean               refreshFromXml;

  FileFinder(String title, String description, String url, List<Dir> dirs, List<MediaFile> pics, MainFrameInterface mainFrame, boolean refreshFromXml)
  {
    this.title          = title;
    this.description    = description;
    this.url            = url;
    this.dirs           = dirs;
    this.pics           = pics;
    this.mainFrame      = mainFrame;
    this.refreshFromXml = refreshFromXml;
  }

  public void run()
  {
    File       currentDir = mainFrame.getCurrentDir();
    List<File> files      = sortFiles(currentDir.listFiles());
    int        i          = 1;

    for (File file : files)
    {
      String fileName = file.getName();

      if (logger.isDebugEnabled())
      {
        logger.debug("Examining file #" + i++ + " of " + files.size() + ": " + fileName);
      }

      mainFrame.setStatus("Examining file #" + i + ": " + fileName);

      if (file.isDirectory())
      {
        addDir(file);
      }
      else
      {
        addMediaFile(file);
      }
    }

    Dir dirpage = mainFrame.getDirpage();

    dirpage.setDirs(dirs);
    dirpage.setPics(pics);
    dirpage.setDescription("");

    try
    {
      if (refreshFromXml)
      {
        mainFrame.getInfoFromXmlFile(true);
      }
    }
    catch (Exception e)
    {
      logger.error("Exception", e);
    }

    mainFrame.setStatus("");
  }

  /** Sort the files by the desired criteria. */
  private List<File> sortFiles(File[] files)
  {
    List<com.nurflugel.picturebrowserservlet.domain.File> theFiles = new ArrayList<com.nurflugel.picturebrowserservlet.domain.File>();

    if (files != null)
    {
      for (File file : files)
      {
        com.nurflugel.picturebrowserservlet.domain.File theFile;
        SortCriteria                                    sortCriteria = mainFrame.getSortCriteria();

        if (file.isDirectory())
        {
          theFile = new Dir(file, title, sortCriteria);
        }
        else
        {
          theFile = new com.nurflugel.picturebrowserservlet.domain.File(file, title, sortCriteria);
        }

        theFiles.add(theFile);
      }

      Collections.sort(theFiles);

      return Arrays.asList(files);
    }
    else
    {
      return new ArrayList<File>();
    }

    // return theFiles;
  }

  /**
   * Adds a media file to the list of files if it isn't already in it.
   *
   * @param  file  the file in question
   */
  private void addMediaFile(File file)
  {
    String  lowerCaseFileName = file.getName().toLowerCase();
    boolean isSystemFile      = UtilMethods.isSystemFile(file.getName());

    if (logger.isDebugEnabled())
    {
      logger.debug("isSystemFile = " + isSystemFile);
    }

    if (!isSystemFile)
    {
      boolean   defaultDisplayExifDropdown = mainFrame.getDirpage().getDefaultDisplayExifDropdown();
      MediaFile mediaFile                  = getMediaFile(lowerCaseFileName, file, title, description, url, defaultDisplayExifDropdown,
                                                          mainFrame.getSortCriteria());

      if (mediaFile != null)
      {
        pics.add(mediaFile);
      }
    }
  }

  private void addDir(File file)
  {
    if (isValidDir(file.getName()))
    {
      Dir dir = new Dir(file, title);

      dirs.add(dir);
    }
  }

  private boolean isValidDir(String fileName)
  {
    return !"thumbnails".equalsIgnoreCase(fileName) && !"previews".equalsIgnoreCase(fileName);
  }
}
