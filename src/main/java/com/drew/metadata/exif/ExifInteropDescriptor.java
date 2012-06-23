/*
 * Created by dnoakes on 12-Nov-2002 22:27:34 using IntelliJ IDEA.
 */
package com.drew.metadata.exif;

import com.drew.metadata.Directory;
import com.drew.metadata.MetadataException;
import com.drew.metadata.TagDescriptor;
import static com.drew.metadata.exif.ExifInteropDirectory.TAG_INTEROP_INDEX;
import static com.drew.metadata.exif.ExifInteropDirectory.TAG_INTEROP_VERSION;

/**  */
public class ExifInteropDescriptor extends TagDescriptor
{
  public ExifInteropDescriptor(Directory directory)
  {
    super(directory);
  }

  public String getDescription(int tagType) throws MetadataException
  {
    switch (tagType)
    {
      case TAG_INTEROP_INDEX:
        return getInteropIndexDescription();

      case TAG_INTEROP_VERSION:
        return getInteropVersionDescription();

      default:
        return _directory.getString(tagType);
    }
  }

  private String getInteropVersionDescription() throws MetadataException
  {
    if (!_directory.containsTag(TAG_INTEROP_VERSION))
    {
      return null;
    }

    int[] ints = _directory.getIntArray(TAG_INTEROP_VERSION);

    return ExifDescriptor.convertBytesToVersionString(ints);
  }

  private String getInteropIndexDescription()
  {
    if (!_directory.containsTag(TAG_INTEROP_INDEX))
    {
      return null;
    }

    String interopIndex = _directory.getString(TAG_INTEROP_INDEX).trim();

    if ("R98".equalsIgnoreCase(interopIndex))
    {
      return "Recommended Exif Interoperability Rules (ExifR98)";
    }
    else
    {
      return "Unknown (" + interopIndex + ")";
    }
  }
}
