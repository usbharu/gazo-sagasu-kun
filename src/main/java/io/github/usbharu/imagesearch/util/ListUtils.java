package io.github.usbharu.imagesearch.util;

import java.util.List;

public class ListUtils {
  private ListUtils(){}

  public static <T> T getOr(List<T> list,int index,T or){
    if (0 > index || index > list.size()) {
      return or;
    }
    return list.get(index);
  }

}
