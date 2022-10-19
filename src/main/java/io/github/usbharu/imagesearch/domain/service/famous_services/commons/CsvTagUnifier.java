package io.github.usbharu.imagesearch.domain.service.famous_services.commons;

import com.github.mygreen.supercsv.io.CsvAnnotationBeanReader;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import io.github.usbharu.imagesearch.domain.service.scan.Unifier;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.annotation.PostConstruct;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.supercsv.prefs.CsvPreference;

@Component
@ConfigurationProperties("imagesearch.scan.unifier")
public class CsvTagUnifier implements Unifier {

  private String path = null;

  // Keyが統一前,Valueが統一後
  Map<String, String> planeUnifier = new HashMap<>();
  // Keyが統一後,Valueが統一前
  Map<String, Pattern> regexUnifier = new HashMap<>();

  public void setPath(String path) {
    this.path = path;
  }

  @Override
  public ImageMetadata unify(ImageMetadata imageMetadata) {
    if (!(imageMetadata instanceof Tags) || planeUnifier.size() + regexUnifier.size() == 0) {
      return imageMetadata;
    }
    Tags metadata = (Tags) imageMetadata;
    for (int i = 0, metadataSize = metadata.size(); i < metadataSize; i++) {
      Tag tag = metadata.get(i);

      if (planeUnifier.containsKey(tag.getName())) {

        metadata.set(i, new Tag(planeUnifier.get(tag.getName())));
      } else {

        for (Entry<String, Pattern> stringPatternEntry : regexUnifier.entrySet()) {
          Matcher matcher = stringPatternEntry.getValue().matcher(tag.getName());
          if (matcher.find()) {
            String name = matcher.replaceAll(stringPatternEntry.getKey());
            metadata.set(i, new Tag(name));
          }
        }

      }
    }
    return metadata;
  }

  @PostConstruct
  public void init() {
    if (path == null) {
      return;
    }
    try (CsvAnnotationBeanReader<UnifierCsv> csv = new CsvAnnotationBeanReader<>(UnifierCsv.class,
        new InputStreamReader(new FileInputStream(path),
            StandardCharsets.UTF_8), CsvPreference.STANDARD_PREFERENCE)) {
      List<UnifierCsv> unifierCsvs = csv.readAll();
      for (UnifierCsv unifierCsv : unifierCsvs) {
        if (unifierCsv.isRegex()) {
          try {
            regexUnifier.put(unifierCsv.unifiedString, Pattern.compile(unifierCsv.string));
          }catch (PatternSyntaxException e){
            e.printStackTrace();
          }
        } else {
          planeUnifier.put(unifierCsv.string, unifierCsv.unifiedString);
        }
      }
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }

  }
}
