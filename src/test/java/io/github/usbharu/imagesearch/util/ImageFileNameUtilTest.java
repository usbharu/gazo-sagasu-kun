package io.github.usbharu.imagesearch.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class ImageFileNameUtilTest {

  @Test
  void getTwitterFileUserName_TwitterFile_returnUserName() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String twitterFileUserName =
        imageFileNameUtil.getTwitterFileUserName("usbharu_dev-1571732741414334464-img1.png");
    assertEquals("usbharu_dev",twitterFileUserName);
  }

  @Test
  void getTwitterFileUserName_NonTwitterFile_returnNull() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String userName =
        imageFileNameUtil.getTwitterFileUserName("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
    assertNull(userName);
  }

  @Test
  void getTwitterFIneId_TwitterFile_returnId() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String twitterFileId =
        imageFileNameUtil.getTwitterFileId("usbharu_dev-1571732741414334464-img1.png");
    assertEquals("1571732741414334464",twitterFileId);
  }

  @Test
  void getTwitterFileNumber_TwitterFile_returnNumber() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String twitterFileNumber =
        imageFileNameUtil.getTwitterFileNumber("usbharu_dev-1571732741414334464-img1.png");
    assertEquals("1",twitterFileNumber);
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
    assertEquals(url,imageFileNameUtil.getTwitterUrl(name));
  }

  @Test
  void getPixivUrl_PixivFile_returnPixivUrl() {
    ImageFileNameUtil imageFileNameUtil = new ImageFileNameUtil();
    String url = "https://www.pixiv.net/artworks/123456";
    String name = "123456_p0.png";
    assertEquals(url,imageFileNameUtil.getPixivUrl(name));
  }
}
