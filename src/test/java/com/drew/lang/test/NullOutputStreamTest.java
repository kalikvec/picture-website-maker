/**
 * Created by IntelliJ IDEA.
 * User: dnoakes
 * Date: Dec 15, 2002
 * Time: 3:30:02 PM
 * To change this template use Options | File Templates.
 */
package com.drew.lang.test;

import com.drew.lang.NullOutputStream;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.OutputStream;

@Test(groups = "unit")
public class NullOutputStreamTest
{
  public void testCreateNullOutputStream() throws Exception
  {
    OutputStream out = new NullOutputStream();

    out.write(1);
    Assert.assertTrue(true);
  }
}
