package com.nurflugel.picturebrowserservlet.gui;

import java.awt.*;
import javax.swing.*;

/**  */
class ScrollablePicture extends JLabel implements Scrollable
{
  /** Use serialVersionUID for interoperability. */
  private static final long serialVersionUID = -3716886942569716851L;
  private int               maxUnitIncrement = 1;

  /** Creates a new ScrollablePicture object. */
  ScrollablePicture(ImageIcon i, int m)
  {
    super(i);
    maxUnitIncrement = m;
  }

  // ------------------------ INTERFACE METHODS ------------------------
  // --------------------- Interface Scrollable ---------------------
  /**  */
  public Dimension getPreferredScrollableViewportSize()
  {
    return getPreferredSize();
  }

  /**  */
  public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction)
  {
    // Get the current position.
    int currentPosition = 0;

    if (orientation == HORIZONTAL)
    {
      currentPosition = visibleRect.x;
    }
    else
    {
      currentPosition = visibleRect.y;
    }

    // Return the number of pixels between currentPosition
    // and the nearest tick mark in the indicated direction.
    if (direction < 0)
    {
      int newPosition = currentPosition - ((currentPosition / maxUnitIncrement) * maxUnitIncrement);

      return (newPosition == 0) ? maxUnitIncrement
                                : newPosition;
    }
    else
    {
      return (((currentPosition / maxUnitIncrement) + 1) * maxUnitIncrement) - currentPosition;
    }
  }

  /**  */
  public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction)
  {
    if (orientation == HORIZONTAL)
    {
      return visibleRect.width - maxUnitIncrement;
    }
    else
    {
      return visibleRect.height - maxUnitIncrement;
    }
  }

  /**  */
  public boolean getScrollableTracksViewportWidth()
  {
    return false;
  }

  /**  */
  public boolean getScrollableTracksViewportHeight()
  {
    return false;
  }
  // ------------------------ GETTER/SETTER METHODS ------------------------

  /**  */
  public void setMaxUnitIncrement(int pixels)
  {
    maxUnitIncrement = pixels;
  }
}
