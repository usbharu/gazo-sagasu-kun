package io.github.usbharu.imagesearch.domain.service.famous_services.pixiv;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.LinkTo;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * PixivBatchDownloader用のメタデータパーサ.<br>
 *
 * <a href="https://github.com/xuejianxianzun/PixivBatchDownloader">https://github.com/xuejianxianzun/PixivBatchDownloader</a> でダウンロードされる以下のような形式のメタデータをパースします。
 *
 * <pre>
 * {@code
 *
 * Id
 * 123456
 *
 * Title
 * This is test.
 *
 * User
 * test-User
 *
 * UserId
 * 54321
 *
 * URL
 * https://www.pixiv.net/i/1
 *
 * Tags
 * #test
 * #test1
 * #test2
 * #pixiv
 * #test1000users入り
 *
 * Date
 * 2020-11-07T13:40:15+00:00
 *
 * Description
 *
 * This is test.This is test.
 * }
 * </pre>
 *
 * @since 0.0.4
 * @author usbharu
 */
public class PixivMetadataParser {

  private final static Logger logger = LoggerFactory.getLogger(PixivMetadataParser.class);

  /**
   * メタデータをパースします。
   *
   * @param metaFile パースするメタデータのファイル
   * @return パースされたメタデータのリスト. Nullが帰ることはなく、空のリストが帰ります。
   */
  public static List<ImageMetadata> parse(File metaFile) {
    Objects.requireNonNull(metaFile,"File is Null");
    if (!metaFile.exists()) {
      throw new UncheckedIOException("MetaFile is not Found",new FileNotFoundException());
    }

    if (!metaFile.isFile()) {
      throw new IllegalArgumentException("MetaFile is not File");
    }

    List<ImageMetadata> imageMetadataList = new ArrayList<>();
    try (BufferedReader bufferedReader = Files.newBufferedReader(metaFile.toPath())) {
      String text;
      while ((text = bufferedReader.readLine()) != null) {
        final ImageMetadata imageMetadata = parseData(text, bufferedReader);
        if (imageMetadata != null) {
          imageMetadataList.add(imageMetadata);
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException("MetaFile has problem",e);
    }
    return imageMetadataList;
  }

  private static ImageMetadata parseData(String text, BufferedReader bufferedReader) throws IOException {
    logger.trace("text: {}",text);
    if (text.startsWith("Tags")) {
      logger.trace("Find Tags");
      return tags(bufferedReader);
    }else if (text.startsWith("URL")){
      logger.trace("Find URL");
      return url(bufferedReader);
    }

    return null;
  }

  private static LinkTo url(BufferedReader bufferedReader) throws IOException {
    return new LinkTo(bufferedReader.readLine());
  }

  private static Tags tags(BufferedReader bufferedReader) throws IOException {
    Tags tags = new Tags();
    while (true) {
      String s = bufferedReader.readLine();
      if (!s.startsWith("#")){
        break;
      }
      tags.add(new Tag(s.substring(1)));
    }
    logger.trace("Tags: {}",tags);
    return tags;
  }

}
