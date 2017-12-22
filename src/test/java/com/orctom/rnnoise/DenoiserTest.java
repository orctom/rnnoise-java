package com.orctom.rnnoise;

import com.google.common.io.ByteStreams;
import com.google.common.io.Files;
import com.sun.jna.Pointer;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

public class DenoiserTest {

  @Test
  public void process() throws Exception {
    try (Denoiser denoiser = new Denoiser()) {
      InputStream in = DenoiserTest.class.getResourceAsStream("/sample.wav");
      byte[] bytes = ByteStreams.toByteArray(in);
      int frameSize = 480 * 2;
      int startIndex = 0;
      int endIndex = frameSize;
      boolean isFistFrame = true;
      byte[] output = new byte[bytes.length];
      while (startIndex < bytes.length) {
        if (endIndex > bytes.length) {
          endIndex = bytes.length;
        }
        byte[] data = Arrays.copyOfRange(bytes, startIndex, endIndex);
        byte[] dataOut = denoiser.process(data);

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
        System.out.println("deleted: " + deleted);
      }

      Files.write(output, file);
      System.out.println("Processed wrote to: " + file.getAbsolutePath());
    } catch (Exception e) {
      e.printStackTrace();
    }
    System.out.println("done.");
  }
}
