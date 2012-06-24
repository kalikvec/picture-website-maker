package com.nurflugel.picturebrowserservlet.domain;

import com.nurflugel.picturebrowserservlet.gui.MainFrame;
import com.nurflugel.picturebrowserservlet.gui.SortCriteria;
import com.nurflugel.picturebrowserservlet.htmlstuff.NurFont;
import com.nurflugel.picturebrowserservlet.util.UtilMethods;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import static com.nurflugel.picturebrowserservlet.util.UtilMethods.addToLines;

public class GraphicFile extends MediaFile
{
  private static final long serialVersionUID = -3584841079234069701L;

  public GraphicFile(File file, String title, String description, String url, SortCriteria sortCriteria)
  {
    super(file, title, description, url, sortCriteria);
  }

  protected void writeFileNameHtml(Collection lines, int indentLevel, String fileName, String thumbnailName, String dropdownText, MainFrame mainFrame)
                            throws IOException
  {
    NurFont mainFont = mainFrame.getSkin().getMainFont();

    addToLines(indentLevel,
               "<A HREF=\"" + fileName + "\"><IMG SRC=\"./" + thumbnailName + "\" ALIGN=\"BOTTOM\" BORDER=0 ALT=\"" + fileName
                 + "\"></A><BR><FONT FACE=\"" + mainFont.getFontName() + "\">" + fileName + "</FONT><br>", lines);
    addToLines(indentLevel, dropdownText + " <BR>", lines);
  }
}
