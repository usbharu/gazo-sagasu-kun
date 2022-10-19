package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.service.scan.Filter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.supercsv.prefs.CsvPreference;

@Component
@ConfigurationProperties("imagesearch.scan.filter")
public class CsvTagFilter implements Filter {

  private String path = null;

  List<Pattern> patterns = new ArrayList<>();
  List<String> simpleFilter = new ArrayList<>();

  private static final Logger LOGGER = LoggerFactory.getLogger(CsvTagFilter.class);

  public CsvTagFilter() {

  }

  // TODO: 2022/10/19 一度TagをStringにしてしまって、replaceAllですべて消してしまってからもう一度tagsに戻すほうが早いかもしれない
  @Override
  public ImageMetadata filter(ImageMetadata imageMetadata) {
    //フィルタリングの処理は重たいため、フィルターが空の場合はすぐに返す
    if (!(imageMetadata instanceof Tags) || patterns.size() + simpleFilter.size() == 0) {
      return imageMetadata;
    }

    Tags metadata = (Tags) imageMetadata;
    int size = metadata.size();
    LOGGER.trace("Filtering : {}", metadata);
    for (int i = 0, metadataSize = metadata.size(); i < metadataSize; i++) {
      Tag tag = metadata.get(i);

      if (simpleFilter.contains(tag.getName())) {
        LOGGER.trace("Removed Tag by plane filter : {}", tag.getName());
        metadata.set(i, null);
      } else {

        for (Pattern pattern : patterns) {
          if (pattern.matcher(tag.getName()).find()) {
            LOGGER.trace("Removed Tag by regex filter : {}", tag.getName());
            metadata.set(i, null);
          }
        }

      }
    }
    metadata.removeIf(Objects::isNull);
    LOGGER.debug("Filtered {} (before {}, after {})", size - metadata.size(), size,
        metadata.size());
    return metadata;

  }

  public void setPath(String path) {
    this.path = path;
  }

  @PostConstruct
  public void init() {
    if (path == null) {
      LOGGER.info("Filter does not exist.");
      return;
    }
    LOGGER.info("Filter Path : {}", path);
    try (
        CsvAnnotationBeanReader<FilterCsv> csvListReader =
            new CsvAnnotationBeanReader<>(FilterCsv.class,
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8),
                CsvPreference.STANDARD_PREFERENCE)) {
      List<FilterCsv> filterCsvs = csvListReader.readAll();
      for (FilterCsv filterCsv : filterCsvs) {

        if (filterCsv.getRegex()) {
          try {
            patterns.add(Pattern.compile(filterCsv.getString()));
          }catch (PatternSyntaxException e){
            LOGGER.warn("Filter CSV has regex error",e);
          }
          LOGGER.debug("Add regex filter : {}", filterCsv.getString());
        } else {

          simpleFilter.add(filterCsv.getString());
          LOGGER.debug("Add plane filter : {}", filterCsv.getString());
        }

        LOGGER.info("{} filters have been set.(plane {}, regex {})", filterCsvs.size(),
            simpleFilter.size(), patterns.size());
      }
    }catch (FileNotFoundException e){
      LOGGER.warn("Filter Csv Not Found",e);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
