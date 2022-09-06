package io.github.usbharu.imagesearch.domain.service.pixiv;

import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
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

public class PixivMetadataParser {

  private final static Logger logger = LoggerFactory.getLogger(PixivMetadataParser.class);
  public static List<ImageMetadata> parse(File metaFile) {
    Objects.requireNonNull(metaFile,"File is Null");
    if (!metaFile.exists()) {
      throw new UncheckedIOException("metaFile is not Found",new FileNotFoundException());
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
    }

    return null;
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
