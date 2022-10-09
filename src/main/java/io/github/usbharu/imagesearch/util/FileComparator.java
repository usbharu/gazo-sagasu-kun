package io.github.usbharu.imagesearch.util;

import java.io.File;
import java.util.Comparator;

public class FileComparator implements Comparator<File> {

  private static final ImageFileNameUtil ifnu = new ImageFileNameUtil();

  @Override
  public int compare(File o1, File o2) {
    if (o1.equals(o2)) {
      return 0;
    } else if (ifnu.isPixivTypeFileName(o1.getName()) && ifnu.isPixivTypeFileName(o2.getName())) {

      String o1BaseName = ifnu.getPixivTypeFileBaseName(o1.getName());
      String o2BaseName = ifnu.getPixivTypeFileBaseName(o2.getName());
      String o1Number = ifnu.getPixivFileNumber(o1.getName());
      String o2Number = ifnu.getPixivFileNumber(o2.getName());
      //
      if (o1BaseName.equals(o2BaseName)) {
        return Integer.valueOf(o1Number).compareTo(Integer.valueOf(o2Number));
      } else {
        return o1BaseName.compareTo(o2BaseName);
      }
    }
    return o1.compareTo(o2);
  }
}
