package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import com.github.mygreen.supercsv.annotation.CsvBean;
import com.github.mygreen.supercsv.annotation.CsvColumn;
import com.github.mygreen.supercsv.annotation.format.CsvBooleanFormat;

@CsvBean()
public class FilterCsv {
  @CsvColumn(number = 1)
  private String string;
  @CsvColumn(number = 2)
  @CsvBooleanFormat(failToFalse = true)
  private Boolean isRegex;

  public String getString() {
    return string;
  }

  public void setString(String string) {
    this.string = string;
  }

  public Boolean getRegex() {
    return isRegex;
  }

  public void setIsRegex(Boolean regex) {
    isRegex = regex;
  }
}
