package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.gui.MainFrame;
import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import com.nurflugel.picturebrowserservlet.htmlstuff.NurFont;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

public class GraphicFile extends MediaFile
{
  public GraphicFile(File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
  }

  protected void writeFileNameHtml(DataOutputStream dataOutputStream, String fileName, String thumbnailName, String dropdownText, MainFrame mainFrame)
                            throws IOException
  {
    NurFont mainFont = mainFrame.getSkin().getMainFont();

    dataOutputStream.writeBytes("       <A HREF=\"" + fileName + "\"><IMG SRC=\"./" + thumbnailName + "\" ALIGN=\"BOTTOM\" BORDER=0 ALT=\"" + fileName
                                  + "\"></A><BR><FONT FACE=\"" + mainFont.getFontName() + "\">" + fileName + "</FONT><br>\n");
    dataOutputStream.writeBytes("                                   " + dropdownText + " <BR>\n");
  }
}
