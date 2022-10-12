package io.github.usbharu.imagesearch.util;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.nio.file.FileVisitOption;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class FileComparatorTest {

  @ParameterizedTest
  @ValueSource(strings = {"6u18O","1Q66O0","ZbcAR735","123456789_p0.jpg"})
  void compare_sameFile_returnZero(String string) {
    FileComparator fileComparator = new FileComparator();
    File file = new File(string);
    assertEquals(0,fileComparator.compare(file, file));
  }

  @ParameterizedTest
  @ValueSource(strings = {"Qbu","hQXE08","Bgrg","762842_p3.jpg"})
  void compare_samePathFile_returnZero(String arg1) {
    FileComparator fileComparator = new FileComparator();
    File file = new File(arg1);
    File file1 = new File(arg1);
    assertEquals(0,fileComparator.compare(file,file1));
  }

  @ParameterizedTest
  @CsvSource({
      "r7K,4l9hGZ0",
      "8Hp5B3,m4if20jY",
      "0yzIkT8.jpg,5aSx.png"
  })
  void compare_NotPixivTypeAndNotTheEqual_returnComparedToFile(String arg1,String arg2) {
    FileComparator fileComparator = new FileComparator();
    File file = new File(arg1);
    File file1 = new File(arg2);
    int expected = file.compareTo(file1);
    assertEquals(expected,fileComparator.compare(file,file1));
  }

  @ParameterizedTest
  @CsvSource({
      "1234567_p0.jpg,1234567_p1.jpg,-1",
      "438883483_p38834.png,438883483_p34.jpg,1",
      "87654321_p1.jpg,87654321_p10.jpg,-1"
  })
  void compare_pixivTypeSameBaseName_returnComparedToNumber(String file1,String file2,int expected) {
    FileComparator fileComparator = new FileComparator();
    File file = new File(file1);
    File file3 = new File(file2);
    int actual = fileComparator.compare(file,file3);
    assertEquals(expected,actual);
  }

  @ParameterizedTest
  @CsvSource({
      "123456_p0.png,123457_p0.png,-1",
      "123458_p1.png,123459_p0.png,-1",
      "123460_p0.png,123461_p0.jpg,-1",
      "123462_p34.jpg,123463_p0.png,-1"
  })
  void compare_pixivTypeNonEqualBaseName_returnComparedToBaseName(String arg1,String arg2,int expected) {
    FileComparator fileComparator = new FileComparator();
    File file = new File(arg1);
    File file1 = new File(arg2);
    int actual = fileComparator.compare(file, file1);
    assertEquals(expected,actual);
  }
}
