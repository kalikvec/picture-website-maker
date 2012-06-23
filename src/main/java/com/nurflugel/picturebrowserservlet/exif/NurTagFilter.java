/*
 * Created by dnoakes on 05-Nov-2002 18:57:14 using IntelliJ IDEA.
 */
package com.nurflugel.picturebrowserservlet.exif;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.nurflugel.picturebrowserservlet.LogFactory;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**  */
public class NurTagFilter
{
  private Logger   logger   = LogFactory.getInstance(NurTagFilter.class);
  private Metadata metadata;

  @SuppressWarnings({ "CatchGenericClass", "OverlyBroadCatchBlock" })
  public NurTagFilter(File file)
  {
    String fileName = file.getAbsolutePath();

    if (fileName.toLowerCase().endsWith(".jpg"))
    {
      if (StringUtils.isNotBlank(fileName))
      {
        File jpegFile = new File(fileName);

        try
        {
          metadata = JpegMetadataReader.readMetadata(jpegFile);
        }
        catch (Exception e)
        {
          logger.error("error   ", e);
        }
      }
    }
  }

  private void printImageTags()
  {
    // iterate over the exif data and print to System.out
    List<NurTag> taglist = getInterestingTags();

    for (NurTag nurTag : taglist)
    {
      if (logger.isDebugEnabled())
      {
        logger.debug(nurTag.toString());
      }
    }
  }

  public List<NurTag> getInterestingTags()
  {
    List<NurTag> taglist = new ArrayList<NurTag>();

    if (metadata != null)
    {
      Iterator directories = metadata.getDirectoryIterator();

      while (directories.hasNext())
      {
        Directory directory = (Directory) directories.next();
        Iterator  tags      = directory.getTagIterator();

        while (tags.hasNext())
        {
          Tag tag = (Tag) tags.next();

          // System.out.println("potentially interseting tag = " + tag);
          NurTag interestingTag = null;

          try
          {
            interestingTag = getInterestingTag(tag);
          }
          catch (Exception e)
          {
            logger.error("Couldn't read a image tag", e);
          }

          if (interestingTag != null)
          {
            taglist.add(interestingTag);
          }
        }

        if (directory.hasErrors())
        {
          Iterator errors = directory.getErrors();

          while (errors.hasNext())
          {
            logger.error("ERROR: " + errors.next());
          }
        }
      }
    }

    return taglist;
  }

  private static NurTag getInterestingTag(Tag tag)
  {
    String tagText = tag.toString();
    NurTag keeper  = null;

    for (NurTagEnum interestingTag : NurTagEnum.values())
    {
      if (tagText.startsWith(interestingTag.toString()))
      {
        keeper = new NurTag(interestingTag, tagText);

        break;
      }
    }

    return keeper;
  }

  /**
   * Executes the sample usage program.
   *
   * @param  args  command line parameters
   */
  public static void main(String[] args)
  {
    NurTagFilter nurTagFilter = new NurTagFilter(new File("L:\\Panaormas\\Nike Prefontane\\Pict0032.jpg"));

    nurTagFilter.printImageTags();
  }
}
