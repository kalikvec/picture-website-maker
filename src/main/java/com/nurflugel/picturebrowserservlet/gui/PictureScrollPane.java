package com.nurflugel.picturebrowserservlet.gui;

import com.nurflugel.picturebrowserservlet.LogFactory;
import org.apache.log4j.Category;
import javax.swing.*;
import static java.awt.Adjustable.VERTICAL;

/**  */
public class PictureScrollPane extends JScrollPane
{
  private Category logger = LogFactory.getInstance(PictureScrollPane.class);
  private double   speed  = 20.0;

  // ------------------------ OTHER METHODS ------------------------
  public void scrollDown()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("scrollDown");
    }

    JScrollBar verticalScrollBar = getVerticalScrollBar();
    int        unitIncrement     = verticalScrollBar.getUnitIncrement(VERTICAL);
    int        oldValue          = verticalScrollBar.getValue();
    int        value             = oldValue + (int) (unitIncrement * speed);

    setVerticalScrollbarPosition(verticalScrollBar, value, oldValue);
  }

  /**  */
  public void scrollUp()
  {
    if (logger.isDebugEnabled())
    {
      logger.debug("scrollUp");
    }

    JScrollBar verticalScrollBar = getVerticalScrollBar();
    int        unitIncrement     = verticalScrollBar.getUnitIncrement(VERTICAL);
    int        oldValue          = verticalScrollBar.getValue();
    int        value             = oldValue - (int) (unitIncrement * speed);

    setVerticalScrollbarPosition(verticalScrollBar, value, oldValue);
  }

  /**  */
  private void setVerticalScrollbarPosition(JScrollBar verticalScrollBar, int value, int oldValue)
  {
    int minimum  = verticalScrollBar.getMinimum();
    int maximum  = verticalScrollBar.getMaximum();
    int newValue = (value < minimum) ? minimum
                                     : value;

    newValue = (newValue > maximum) ? maximum
                                    : newValue;

    if (newValue != oldValue)
    {
      if (logger.isDebugEnabled())
      {
        logger.debug("setVerticalScrollbarPosition - scrolling to " + newValue);
      }

      verticalScrollBar.setValue(newValue);
    }
  }
}
