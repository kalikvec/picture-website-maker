/*
 * Created by dnoakes on 22-Nov-2002 08:26:26 using IntelliJ IDEA.
 */
package com.drew.metadata.iptc.test;

import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataReader;
import com.drew.metadata.iptc.IptcDirectory;
import com.drew.metadata.iptc.IptcReader;
import org.testng.annotations.Test;
import java.io.File;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/**  */
@Test(groups = "unit")
public class IptcReaderTest
{
  public void testExifReader() throws Exception
  {
    File           iptcFile = new File("src/com/drew/metadata/iptc/test/withIptc.jpg");
    MetadataReader reader   = new IptcReader(iptcFile);
    Metadata       metadata = reader.extract();

    assertTrue(metadata.containsDirectory(IptcDirectory.class));

    Directory directory = metadata.getDirectory(IptcDirectory.class);

    assertEquals("City", directory.getDescription(IptcDirectory.TAG_CITY));
  }
}
