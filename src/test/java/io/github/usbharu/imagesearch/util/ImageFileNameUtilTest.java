package io.github.usbharu.imagesearch.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

class ImageFileNameUtilTest {

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334454.img1.jpg",
      "usbharu_dev-1571732741414334454.img1.JPG", "usbharu_dev-1571732741414334454.img1.jpeg",
      "usbharu_dev-1571732741414334454.img1.JPEG", "DO5vA2.jpg", "1dq7.jpg"})
  void isJpg_JpgFile_returnTrue(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertTrue(imageFileNameUtil.isJpg(string));
  }

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334454.img1.png",
      "usbharu_dev-1571732741414334454.img1.PNG", "9065IRC0.png", "riK.png", "u0RYBn.webp",
      "pb7.gif", "ak9CIdTjpg"})
  void isJpg_NotJpgFile_returnFalse(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertFalse(imageFileNameUtil.isJpg(string));
  }

  @Test
  void isJpg_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class, () -> imageFileNameUtil.isJpg(null));
  }

  @Test
  void isJpg_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.isJpg(""));
  }

  @Test
  void isJpg_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.isJpg(" "));
  }

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334464-img1.png",
      "usbharu_dev-1571732741414334464-img1.PNG", "0Wtx0Yc3.png", "qF00O.png"})
  void isPng_PngFile_returnTrue(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertTrue(imageFileNameUtil.isPng(string));
  }

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334464-img1.jpg",
      "usbharu_dev-1571732741414334464-img1.JPG", "usbharu_dev-1571732741414334464-img1.JPEG",
      "usbharu_dev-1571732741414334464-img1.jpeg", "l2dpng"})
  void isPng_NotPngFile_returnFalse(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertFalse(imageFileNameUtil.isPng(string));
  }

  @Test
  void isPng_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class, () -> imageFileNameUtil.isPng(null));
  }

  @Test
  void isPng_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.isPng(""));
  }

  @Test
  void isPng_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.isPng(" "));
  }

  @ParameterizedTest
  @ValueSource(strings = {"535932_p0.jpg", "1_p1.jpg", "11111111111111_p2222222222.png"})
  void isPixivTypeFileName_PixivFile_returnTrue(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThat(imageFileNameUtil.isPixivTypeFileName(string)).isTrue();
  }

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334464-img1.jpg", "1234567890_.jpg",
      "65543345543_pa.png", "346899875544_p1", "4828229592p1.jpg"})
  void isPixivTypeFileName_NotPixivFile_returnFalse(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertFalse(imageFileNameUtil.isPixivTypeFileName(string));
  }

  @Test
  void isPixivTypeFileName_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.isPng(""));
  }

  @Test
  void isPixivTypeFileName_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.isPng(" "));
  }

  @Test
  void isPixivTypeFileName_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class, () -> imageFileNameUtil.isPixivTypeFileName(null));
  }

  @ParameterizedTest
  @CsvSource({
      "1234567_p0.png,1234567",
      "516_p1.jpg,516"
  })
  void getPixivTypeFileBaseName_pixivFileName_returnBaseName(String string, String expected) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertEquals(expected, imageFileNameUtil.getPixivTypeFileBaseName(string));
  }

  @ParameterizedTest
  @ValueSource(strings = {"3ZcLIVoe.jpg", "WU9_p0.jpg"})
  void getPixivTypeFileBaseName_NotPixivFileName_returnNull(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertNull(imageFileNameUtil.getPixivTypeFileBaseName(string));
  }

  @Test
  void getPixivTypeFileBaseName_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class,
        () -> imageFileNameUtil.getPixivTypeFileBaseName(null));
  }

  @Test
  void getPixivTypeFileBaseName_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class,
        () -> imageFileNameUtil.getPixivTypeFileBaseName(""));
  }

  @Test
  void getPixivTypeFileBaseName_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class,
        () -> imageFileNameUtil.getPixivTypeFileBaseName(" "));
  }

  @ParameterizedTest
  @CsvSource({
      "136834_p1.jpg,1",
      "7193799_p3424.png,3424"
  })
  void getPixivFileNumber_pixivFile_returnNumber(String string, String expected) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertEquals(expected, imageFileNameUtil.getPixivFileNumber(string));
  }

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334464-img1.png", "mt6qp.jpg",
      "4Dba622S_p0.jpg"})
  void getPixivFileNumber_NotPixivFile_returnNull(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertNull(imageFileNameUtil.getPixivFileNumber(string));
  }

  @Test
  void getPixivFileNumber_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class, () -> imageFileNameUtil.getPixivFileNumber(null));
  }

  @Test
  void getPixivFileNumber_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.getPixivFileNumber(""));
  }

  @Test
  void getPixivFileNumber_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.getPixivFileNumber(" "));
  }

  @Test
  void getTwitterFileUserName_TwitterFile_returnUserName() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String twitterFileUserName =
        imageFileNameUtil.getTwitterFileUserName("usbharu_dev-1571732741414334464-img1.png");
    assertEquals("usbharu_dev", twitterFileUserName);
  }

  @Test
  void getTwitterFileUserName_NonTwitterFile_returnNull() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String userName =
        imageFileNameUtil.getTwitterFileUserName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    assertNull(userName);
  }

  @Test
  void getTwitterFileUserName_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class, () -> imageFileNameUtil.getTwitterFileUserName(null));
  }

  @Test
  void getTwitterFileUserName_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class,
        () -> imageFileNameUtil.getTwitterFileUserName(""));
  }

  @Test
  void getTwitterFileUserName_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class,
        () -> imageFileNameUtil.getTwitterFileUserName(" "));
  }

  @Test
  void getTwitterFIneId_TwitterFile_returnId() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String twitterFileId =
        imageFileNameUtil.getTwitterFileId("usbharu_dev-1571732741414334464-img1.png");
    assertEquals("1571732741414334464", twitterFileId);
  }

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334464-img.png",
      "usbharu_dev-1571732741414334464-imga1.png", "usbharu_dev--1571732741414334464-img1.png",
      "123456789_p0.jpg"})
  void getTwitterFileId_NotTwitterFile_returnNull(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertNull(imageFileNameUtil.getTwitterFileId(string));
  }

  @Test
  void getTwitterFileId_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class, () -> imageFileNameUtil.getTwitterFileId(null));
  }

  @Test
  void getTwitterFileId_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.getTwitterFileId(""));
  }

  @Test
  void getTwitterFileId_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.getTwitterFileId(" "));
  }

  @Test
  void getTwitterFileNumber_TwitterFile_returnNumber() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String twitterFileNumber =
        imageFileNameUtil.getTwitterFileNumber("usbharu_dev-1571732741414334464-img1.png");
    assertEquals("1", twitterFileNumber);
  }

  @ParameterizedTest
  @ValueSource(strings = {"usbharu_dev-1571732741414334464-img.png",
      "usbharu_dev-1571732741414334464-imga1.png", "usbharu_dev--1571732741414334464-img1.png",
      "123456789_p0.jpg"})
  void getTwitterFileNumber_NotTwitterFile_returnNull(String string) {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertNull(imageFileNameUtil.getTwitterFileNumber(string));
  }

  @Test
  void getTwitterFileNumber_Null_throwNullPointException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(NullPointerException.class, () -> imageFileNameUtil.getTwitterFileNumber(null));
  }

  @Test
  void getTwitterFileNumber_Empty_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.getTwitterFileNumber(""));
  }

  @Test
  void getTwitterFileNumber_Blank_throwIllegalArgumentException() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    assertThrows(IllegalArgumentException.class, () -> imageFileNameUtil.getTwitterFileNumber(" "));
  }

  @Test
  void isTwitterTypeFileName_TwitterFile_returnTrue() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    boolean isTwitter =
        imageFileNameUtil.isTwitterTypeFileName("usbharu_dev-1571732741414334464-img1.png");
    assertTrue(isTwitter);
  }

  @Test
  void isTwitterTypeFileName_NonTwitterFile_returnFalse() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    boolean isTwitter =
        imageFileNameUtil.isTwitterTypeFileName("NPn26y7");
    assertFalse(isTwitter);
  }

  @Test
  void getTwitterUrl_TwitterFile_returnTwitterUrl() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String url = "https://twitter.com/usbharu_dev/status/1571732741414334464/photo/1";
    String name = "usbharu_dev-1571732741414334464-img1.png";
    assertEquals(url, imageFileNameUtil.getTwitterUrl(name));
  }

  @Test
  void getPixivUrl_PixivFile_returnPixivUrl() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String url = "https://www.pixiv.net/artworks/123456";
    String name = "123456_p0.png";
    assertEquals(url, imageFileNameUtil.getPixivUrl(name));
  }
}
