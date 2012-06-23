/*
 * Created by dnoakes on 26-Nov-2002 11:21:43 using IntelliJ IDEA.
 */
package com.drew.metadata;

/**  */
public interface MetadataReader
{
  Metadata extract() throws MetadataException;
  Metadata extract(Metadata metadata) throws MetadataException;
}
