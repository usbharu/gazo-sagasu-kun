package io.github.usbharu.imagesearch.domain.service.scan;

import io.github.usbharu.imagesearch.domain.model.Image;
import java.io.File;
import java.nio.file.Path;
import org.springframework.stereotype.Component;


/**
 * 画像のメタデータを取得するスキャナーの定義
 *
 * @author usbharu
 * @since 0.0.4
 */
@Component
@Deprecated
public interface Scanner {

  /**
   * 画像にスキャナーが対応しているかを返します. このメソッドでは簡易的な判定で良いです。
   *
   * @param file 判定する画像
   * @return 対応していたらtrue boolean
   * @see #getMetadata(File, Path) #getMetadata(File, Path)
   */
  boolean isSupported(File file);


  /**
   * メタデータを取得します.<br>
   *
   * @param image   メタデータを取得する画像のファイル
   * @param subpath {@link Image#Image(int, String, String, int)} の {@code path} に代入するパス
   * @return メタデータが代入された {@link Image} nullが返却されることもある。
   */
  Image getMetadata(File image, Path subpath);
}
