package io.github.usbharu.imagesearch.util;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ImageTagUtil {

  private ImageTagUtil() {
  }

  public static Tags parseTags(List<Map<String, Object>> maps) {
    Tags tags = new Tags();
    for (Map<String, Object> map : maps) {
      tags.add(parseTag(map));
    }
    return tags;
  }

  public static Tag parseTag(Map<String, Object> map) {
    return new Tag((Integer) map.get("id"), (String) map.get("name"));
  }

  public static Image parseImage(Map<String, Object> map) {
    return new Image((Integer) map.get("image_id"), (String) map.get("image_name"),
        (String) map.get("image_path"), (Integer) map.get("image_group"));
  }

  public static List<Image> parseImages(List<Map<String, Object>> maps) {
    List<Image> images = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      images.add(parseImage(map));
    }
    return images;
  }

  public static List<Image> parseImagesWithMetadata(List<Map<String,Object>> maps){
    List<Image> images = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      images.add(parseImageWithMetadata(map));
    }
    return images;
  }

  public static Image parseImageWithMetadata(Map<String, Object> map) {
    Image image = parseImage(map);
    Tags tags = getTagsNoNull(image);
    tags.add(getTag(map));
    return image;
  }

  public static Tag getTag(Map<String, Object> map) {
    return new Tag((Integer) map.get("id"), (String) map.get("name"));
  }

  public static Tags getTags(Image image) {
    for (ImageMetadata metadatum : image.getMetadata()) {
      if (metadatum instanceof Tags) {
        return ((Tags) metadatum);
      }
    }
    return null;
  }

  public static Tags getTagsNoNull(Image image) {
    Tags tags = getTags(image);
    if (tags == null) {
      return new Tags();
    }
    return tags;
  }

  public static Tags getTags(List<ImageMetadata> metadataList) {
    for (ImageMetadata imageMetadata : metadataList) {
      Tags tagsNoNull = getTagsNoNull(imageMetadata);
      if (tagsNoNull.isEmpty()) {
        continue;
      }
      return tagsNoNull;
    }
    return null;
  }

  public static Tags getTagsNoNull(List<ImageMetadata> metadataList) {
    Tags tags = getTags(metadataList);
    if (tags == null) {
      return new Tags();
    }
    return tags;
  }

  public static Tags getTags(ImageMetadata metadata) {
    if (metadata instanceof Tags) {
      return (Tags) metadata;
    }
    return null;
  }

  public static Tags getTagsNoNull(ImageMetadata metadata) {
    if (getTags(metadata) == null) {
      return new Tags();
    }
    return (Tags) metadata;
  }

}
