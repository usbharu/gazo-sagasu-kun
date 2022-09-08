package io.github.usbharu.imagesearch.domain.service.scan;

import static org.junit.jupiter.api.Assertions.*;

import io.github.usbharu.imagesearch.util.ImageFileNameUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

class DefaultJpegScannerTest extends ScannerTest{
  @Spy
  ImageFileNameUtil imageFileNameUtil;

  @InjectMocks DefaultJpegScanner defaultJpegScanner;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    scanner = defaultJpegScanner;
  }
}
