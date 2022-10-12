package io.github.usbharu.imagesearch.util;

import io.github.usbharu.imagesearch.domain.model.Image;
import io.github.usbharu.imagesearch.domain.model.ImageMetadata;
import io.github.usbharu.imagesearch.domain.model.Tag;
import io.github.usbharu.imagesearch.domain.model.Tags;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ImageTagUtil {

  private ImageTagUtil() {
  }

  public static Image parseImage(Map<String, Object> map) {
    Objects.requireNonNull(map,"Map is Null");
    return new Image((Integer) map.get("image_id"), (String) map.get("image_name"),
        (String) map.get("image_path"), (Integer) map.get("image_group"));
  }

  public static List<Image> parseImages(List<Map<String, Object>> maps) {
    Objects.requireNonNull(maps,"Maps is Null");
    List<Image> images = new ArrayList<>();
    for (Map<String, Object> map : maps) {
      images.add(parseImage(map));
    }
    return images;
  }


  public static Image parseImageWithMetadata(Map<String, Object> map) {
    Objects.requireNonNull(map,"Map is Null");
    Image image = parseImage(map);
    Tags tags = getTagsNoNull(image);
    tags.add(getTag(map));
    return image;
  }

  public static Tag getTag(Map<String, Object> map) {
    Objects.requireNonNull(map,"Map is Null");
    return new Tag((Integer) map.get("id"), (String) map.get("name"));
  }

  public static Tags getTags(Image image) {
    Objects.requireNonNull(image,"Image is Null");
    Tags tags = new Tags();
    for (ImageMetadata metadatum : image.getMetadata()) {
      if (metadatum instanceof Tags) {
        tags.addAll((Tags) metadatum);
      }
    }
    return tags;
  }

  public static Tags getTagsNoNull(Image image) {
    Objects.requireNonNull(image,"Image is Null");
    return getTags(image);
  }

  public static Tags getTags(List<ImageMetadata> metadataList) {
    Objects.requireNonNull(metadataList,"MetadataList is Null");
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
    Objects.requireNonNull(metadataList,"MetadataList is Null");
    Tags tags = getTags(metadataList);
    if (tags == null) {
      return new Tags();
    }
    return tags;
  }

  public static Tags getTags(ImageMetadata metadata) {
    Objects.requireNonNull(metadata,"Metadata is Null");
    if (metadata instanceof Tags) {
      return (Tags) metadata;
    }
    return null;
  }

  public static Tags getTagsNoNull(ImageMetadata metadata) {
    Objects.requireNonNull(metadata,"Metadata is Null");
    if (getTags(metadata) == null) {
      return new Tags();
    }
    return (Tags) metadata;
  }

  public static Tags getMatchedTags(Tags tags,String regex){
    Tags result = new Tags();
    for (Tag tag : tags) {
      if (tag.getName().matches(regex)) {
        result.add(tag);
      }
    }
    return result;
  }

}
