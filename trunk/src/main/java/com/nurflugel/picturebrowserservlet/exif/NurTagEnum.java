package com.nurflugel.picturebrowserservlet.exif;

/**  */
public enum NurTagEnum
{
  Make             ("[Exif] Make - "),
  Model            ("[Exif] Model - "),
  DateTime         ("[Exif] Date/Time - "),
  DateTimeOriginal ("[Exif] Date/Time Original - "),
  DatetimeDigitized("[Exif] Date/Time digitized - "),
  Iso              ("[Exif] ISO Speed Ratings - "),
  ShutterSpeed     ("[Exif] Shutter Speed Value - "),
  ExposureTime     ("[Exif] Exposure Time - "),
  ExposureProgram  ("[Exif] Exposure Program - "),
  SceneType        ("[Exif] Scene Type - "),
  FStopNumber      ("[Exif] F-Number - "),
  ColorSpace       ("[Exif] Color Space - "),
  Aperature        ("[Exif] Aperture Value - "),
  ExposoureBias    ("[Exif] Exposure Bias Value - "),
  MeteringMode     ("[Exif] Metering Mode - "),
  Flash            ("[Exif] Flash - "),
  Width            ("[Exif] Exif Image Width - "),
  Height           ("[Exif] Exif Image Height - "),
  FocalLength      ("[Exif] Focal Length - ");

  private String value;

  NurTagEnum(String value)
  {
    this.value = value;
  }

  public String toString()
  {
    return value;
  }
}
