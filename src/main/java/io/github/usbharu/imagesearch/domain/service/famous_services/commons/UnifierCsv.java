package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;
import com.github.mygreen.supercsv.annotation.format.CsvBooleanFormat;

@CsvBean
public class UnifierCsv {
  @CsvColumn(number = 1)
  String string;
  @CsvColumn(number = 2)
  String unifiedString;
  @CsvColumn(number = 3)
  @CsvBooleanFormat(failToFalse = true)
  boolean isRegex;

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public String getUnifiedString() {
    return unifiedString;
  }

  public void setUnifiedString(String unifiedString) {
    this.unifiedString = unifiedString;
  }

  public boolean isRegex() {
    return isRegex;
  }

  public void setIsRegex(boolean regex) {
    isRegex = regex;
  }
}
