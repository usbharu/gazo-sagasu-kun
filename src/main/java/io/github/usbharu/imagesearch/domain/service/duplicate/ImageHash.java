package io.github.usbharu.imagesearch.domain.service.duplicate;

import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash.Precision;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class ImageHash {

  private static final HashingAlgorithm DIFFERENCE_HASH = new DifferenceHash(32, Precision.Simple);

  public Hash hash(File file) throws IOException {
    return DIFFERENCE_HASH.hash(file);
  }

}
