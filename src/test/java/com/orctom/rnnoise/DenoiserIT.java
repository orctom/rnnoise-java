package com.orctom.rnnoise;

import com.google.common.base.Stopwatch;
import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.InputStream;
import java.util.Arrays;

public class DenoiserIT {
  
  private static final Logger LOGGER = LoggerFactory.getLogger(DenoiserIT.class);

  @Test
  public void process() throws Exception {
    try (Denoiser denoiser = new Denoiser()) {
      InputStream in = DenoiserIT.class.getResourceAsStream("/sample.wav");
      byte[] bytes = ByteStreams.toByteArray(in);
//      int frameSize = 9600;
      int frameSize = 12800;
      int startIndex = 0;
      int endIndex = frameSize;
      boolean isFistFrame = true;
      LOGGER.debug("total: {}", bytes.length);
      byte[] output = new byte[bytes.length];
      while (startIndex < bytes.length) {
        if (endIndex > bytes.length) {
          endIndex = bytes.length;
        }
        byte[] data = Arrays.copyOfRange(bytes, startIndex, endIndex);
        LOGGER.debug("{} --> {}", startIndex, endIndex);
        Stopwatch stopwatch = Stopwatch.createStarted();
        byte[] dataOut = denoiser.process(data);
        LOGGER.debug("took: {}", stopwatch);

        if (isFistFrame) {
          isFistFrame = false;
          startIndex += frameSize;
          endIndex += frameSize;
          continue;
        }
        System.arraycopy(dataOut, 0, output, startIndex, endIndex - startIndex);
        startIndex += frameSize;
        endIndex += frameSize;
      }

      String cwd = System.getProperty("user.dir");
      File file = new File(cwd, "processed.pcm");
      if (file.exists()) {
        boolean deleted = file.delete();
        LOGGER.debug("deleted: {}", deleted);
      }

      Files.write(output, file);
      LOGGER.debug("Processed wrote to: {}", file.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    }
    LOGGER.debug("done.");
  }
}
