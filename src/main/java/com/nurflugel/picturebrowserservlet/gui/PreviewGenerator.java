package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.domain.GraphicFile;
import com.nurflugel.picturebrowserservlet.domain.MediaFile;
import java.io.File;
import java.io.IOException;
import java.util.List;
import javax.swing.*;

/**
 * Created by IntelliJ IDEA. User: douglasbullard Date: May 11, 2007 Time: 6:56:32 PM To change this template use File | Settings | File Templates.
 */
public class PreviewGenerator implements Runnable
{
  private List<MediaFile>    pics;
  private ProgressMonitor    progressMonitor;
  private MainFrameInterface mainFrame;

  @SuppressWarnings("AssignmentToCollectionOrArrayFieldFromParameter")
  public PreviewGenerator(List<MediaFile> pics, ProgressMonitor progressMonitor, MainFrameInterface mainFrame)
  {
    this.pics            = pics;
    this.progressMonitor = progressMonitor;
    this.mainFrame       = mainFrame;
  }

  /** If previews don't already exist for the pics, generate them. */
  public void run()
  {
    try
    {
      if (!pics.isEmpty())
      {
        File previewDir = new File(pics.get(0).getFile().getParentFile(), "previews");

        if (!previewDir.exists())
        {
          previewDir.mkdir();
        }

        ScaledImageWriter imageWriter = new ScaledImageWriter();
        int               i           = 1;

        for (MediaFile pic : pics)
        {
          if (pic instanceof GraphicFile)
          {
            String previewFileName = pic.getFile().getName();
            File   scaledFile      = new File(previewDir, previewFileName);

            imageWriter.getOrCreateImageFromFile(scaledFile, pic.getFile());

            // progressMonitor.setProgress(i++);
          }
        }
      }
    }
    catch (IOException e)  // logger.error("error", e);
    {}
    finally
    {
      mainFrame.stopGlasspane();
    }
  }
}
