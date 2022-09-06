package io.github.usbharu.imagesearch.image.duplicate;

import dev.brachtendorf.jimagehash.hash.Hash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash;
import dev.brachtendorf.jimagehash.hashAlgorithms.DifferenceHash.Precision;
import dev.brachtendorf.jimagehash.hashAlgorithms.HashingAlgorithm;
import java.io.File;
import java.io.IOException;
import org.springframework.stereotype.Service;

@Service
public class ImageHash {

  private final static HashingAlgorithm algorithm = new DifferenceHash(32, Precision.Simple);

  public Hash hash(File file) throws IOException {
    return algorithm.hash(file);
  }

}
