package com.nurflugel.picturebrowserservlet;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import java.net.URL;

/** Created by IntelliJ IDEA. User: Douglas Bullard Date: Jun 22, 2003 Time: 12:21:11 PM To change this template use Options | File Templates. */
public class LogFactory
{
  private static boolean configured;

  /** Creates a new LogFactory object. */
  private LogFactory() {}

  /**  */
  @SuppressWarnings({ "UseOfSystemOutOrSystemErr", "RawUseOfParameterizedType" })
  public static Logger getInstance(Class daClass)
  {
    synchronized (LogFactory.class)
    {
      if (!configured)
      {
        ClassLoader classLoader = LogFactory.class.getClassLoader();
        URL         resource    = classLoader.getResource("conf/log4j.prop");

        System.out.println("resource = " + resource);

        URL resource2 = classLoader.getResource("log4j.prop");

        System.out.println("resource2 = " + resource2);
        PropertyConfigurator.configure(resource2);
        configured = true;
      }
    }

    return Logger.getLogger(daClass.getName());
  }
}
