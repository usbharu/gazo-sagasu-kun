package io.github.usbharu.imagesearch.domain.service.scan;

import io.github.usbharu.imagesearch.domain.model.Image;
import java.io.File;
import java.nio.file.Path;
import org.springframework.stereotype.Component;


@Component
public interface Scanner {

  /**
   * 画像にスキャナーが対応しているかを返します.
   * このメソッドでは簡易的な判定で良いです。
   * @see #getMetadata(File, Path)
   *
   * @param file 判定する画像
   * @return 対応していたらtrue
   */
  boolean isSupported(File file);
  Image getMetadata(File image, Path subpath);
}
