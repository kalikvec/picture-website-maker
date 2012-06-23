package com.nurflugel.picturebrowserservlet.exif;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**  */
public class NurTagList implements Cloneable, Serializable
{
  private static final long serialVersionUID = 1202468077661766742L;
  private List<NurTag>      list             = new ArrayList<NurTag>();

  public NurTagList() {}

  public NurTagList(List<NurTag> collection)
  {
    for (NurTag snippet : collection)
    {
      list.add(snippet);
    }
  }

  public boolean add(NurTag o)
  {
    return list.add(o);
  }

  public void add(int index, NurTag element)
  {
    list.add(index, element);
  }

  public boolean addAll(NurTagList c)
  {
    return list.addAll(c.getCollection());
  }

  private Collection<? extends NurTag> getCollection()
  {
    return list;
  }

  public boolean addAll(int index, NurTagList c)
  {
    return list.addAll(index, c.getCollection());
  }

  public void clear()
  {
    list.clear();
  }

  public boolean contains(NurTag elem)
  {
    return list.contains(elem);
  }

  public boolean containsAll(NurTagList c)
  {
    return list.containsAll(c.getCollection());
  }

  public String getTopTag()
  {
    String exposureTag  = "[Exif] Exposure Time - ";
    String fstopTag     = "[Exif] F-Number - ";
    String apertureTag  = "[Exif] Aperture Value - ";
    String exposureTag2 = "[Exif] Shutter Speed Value - ";
    String exposure     = "";
    String fstop        = "";

    for (int i = 0; i < size(); i++)
    {
      NurTag tag               = get(i);
      int    exposureTagIndex  = tag.getTestValue().indexOf(exposureTag);
      int    exposureTag2Index = tag.getTestValue().indexOf(exposureTag2);
      int    fStopTagIndex     = tag.getTestValue().indexOf(fstopTag);
      int    apertureTagIndex  = tag.getTestValue().indexOf(apertureTag);

      if (exposureTagIndex > -1)
      {
        exposure = tag.getTagValue();
      }

      if (exposureTag2Index > -1)
      {
        exposure = tag.getTagValue();
      }

      if (fStopTagIndex > -1)
      {
        fstop = tag.getTagValue();
      }

      if (apertureTagIndex > -1)
      {
        fstop = tag.getTagValue();
      }
    }

    return fstop + "  " + exposure;
  }

  public int size()
  {
    return list.size();
  }

  public NurTag get(int index)
  {
    return list.get(index);
  }

  public int indexOf(NurTag elem)
  {
    return list.indexOf(elem);
  }

  public int lastIndexOf(NurTag elem)
  {
    return list.lastIndexOf(elem);
  }

  public boolean remove(NurTag o)
  {
    return list.remove(o);
  }

  public NurTag remove(int index)
  {
    return list.remove(index);
  }

  public boolean removeAll(NurTagList c)
  {
    return list.removeAll(c.getCollection());
  }

  public boolean retainAll(NurTagList c)
  {
    return list.retainAll(c.getCollection());
  }

  public NurTag set(int index, NurTag element)
  {
    return list.set(index, element);
  }

  public NurTagList subList(int fromIndex, int toIndex)
  {
    return new NurTagList(list.subList(fromIndex, toIndex));
  }

  public NurTag[] toArray(Object[] a)
  {
    return list.toArray(new NurTag[list.size()]);
  }

  // ------------------------ CANONICAL METHODS ------------------------
  public boolean equals(Object o)
  {
    return list.equals(o);
  }

  public int hashCode()
  {
    return list.hashCode();
  }

  public String toString()
  {
    return list.toString();
  }

  public boolean isEmpty()
  {
    return list.isEmpty();
  }
}
