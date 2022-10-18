package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.service.scan.Filter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.supercsv.prefs.CsvPreference;

@Service
@ConfigurationProperties("aaa.aaa")
public class CsvTagFilter implements Filter {

  private String path = "";

  List<Pattern> patterns = new ArrayList<>();
  List<String> simpleFilter = new ArrayList<>();

  public CsvTagFilter() {

  }

  @Override
  public ImageMetadata filter(ImageMetadata imageMetadata) {
    if (imageMetadata instanceof Tags) {
      Tags metadata = (Tags) imageMetadata;
      System.out.println("metadata = " + metadata);
      for (int i = 0, metadataSize = metadata.size(); i < metadataSize; i++) {
        Tag tag = metadata.get(i);
        System.out.println("tag.getName() = " + tag.getName());
        if (simpleFilter.contains(tag.getName())) {
          System.out.println("tag "+tag.getName()+" is removed!");
          metadata.set(i, null);
        } else {
          for (Pattern pattern : patterns) {
            if (pattern.matcher(tag.getName()).find()) {
              metadata.set(i, null);
            }
          }
        }
      }
      return metadata;
    }
    return imageMetadata;

  }

  public void setPath(String path) {
    this.path = path;
  }

  @PostConstruct
  public void init() {
    try (
        CsvAnnotationBeanReader<FilterCsv> csvListReader =
            new CsvAnnotationBeanReader<>(FilterCsv.class,
                new InputStreamReader(new FileInputStream(path), StandardCharsets.UTF_8),
                CsvPreference.STANDARD_PREFERENCE)) {
      List<FilterCsv> filterCsvs = csvListReader.readAll();
      for (FilterCsv filterCsv : filterCsvs) {
        if (filterCsv.getRegex()) {
          patterns.add(Pattern.compile(filterCsv.getString()));
        } else {
          simpleFilter.add(filterCsv.getString());
        }
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    System.out.println("patterns = " + patterns);
    System.out.println("simpleFilter = " + simpleFilter);
  }
}
