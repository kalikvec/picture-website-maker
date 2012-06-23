package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import org.apache.log4j.Logger;
import javax.swing.*;

/**
 * This is the 3rd version of SwingWorker (also known as SwingWorker 3), an abstract class that you subclass to perform GUI-related work in a
 * dedicated thread. For instructions on and examples of using this class, see: http://java.sun.com/docs/books/tutorial/uiswing/misc/threads.html Note
 * that the API changed slightly in the 3rd version: You must now invoke start() on the SwingWorker after creating it.
 */
public abstract class SwingWorker
{
  private Object    value;  // see getValue(), setValue()
  private ThreadVar threadVar;
  private Logger    logger = LogFactory.getInstance(SwingWorker.class);

  /** Start a thread that will call the <code>construct</code> method and then exit. */
  protected SwingWorker()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("SwingWorker.SwingWorker");
    }

    final Runnable doFinished = new Runnable()
    {
      public void run()
      {
        finished();
      }
    };

    Runnable doConstruct = new Runnable()
    {
      public void run()
      {
        if (logger.isDebugEnabled())
        {
          logger.debug("SwingWorker.doConstruct.run");
        }

        try
        {
          setValue(construct());
        }
        finally
        {
          threadVar.clear();
        }

        SwingUtilities.invokeLater(doFinished);
      }
    };

    Thread t = new Thread(doConstruct);

    threadVar = new ThreadVar(t);
  }

  /** Called on the event dispatching thread (not on the worker thread) after the <code>construct</code> method has returned. */
  public abstract void finished();

  /** Compute the value to be returned by the <code>get</code> method. */
  public abstract Object construct();
  // ------------------------ OTHER METHODS ------------------------

  /**
   * Return the value created by the <code>construct</code> method. Returns null if either the constructing thread or the current thread was
   * interrupted before a value was produced.
   *
   * @return  the value created by the <code>construct</code> method
   */
  public Object get()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("SwingWorker.get");
    }

    while (true)
    {
      Thread t = threadVar.get();

      if (t == null)
      {
        return getValue();
      }

      try
      {
        t.join();
      }
      catch (InterruptedException e)
      {
        Thread.currentThread().interrupt();  // propagate

        return null;
      }
    }
  }

  /** A new method that interrupts the worker thread. Call this method to force the worker to stop what it's doing. */
  public void interrupt()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("SwingWorker.interrupt");
    }

    Thread t = threadVar.get();

    if (t != null)
    {
      t.interrupt();
    }

    threadVar.clear();
  }

  /** Start the worker thread. */
  public void start()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("SwingWorker.start");
    }

    Thread t = threadVar.get();

    if (t != null)
    {
      t.start();
    }
  }
  // ------------------------ GETTER/SETTER METHODS ------------------------

  /** Get the value produced by the worker thread, or null if it hasn't been constructed yet. */
  protected synchronized Object getValue()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("SwingWorker.getValue");
    }

    return value;
  }

  /** Set the value produced by worker thread. */
  private synchronized void setValue(Object x)
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("SwingWorker.setValue");
    }

    value = x;
  }

  /** Class to maintain reference to current worker thread under separate synchronization control. */
  private static class ThreadVar
  {
    private Thread thread;

    ThreadVar(Thread t)
    {
      thread = t;
    }

    synchronized Thread get()
    {
      return thread;
    }

    synchronized void clear()
    {
      thread = null;
    }
  }
}
