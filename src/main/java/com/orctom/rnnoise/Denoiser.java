package com.orctom.rnnoise;

import com.orctom.rnnoise.exception.IllegalFrameSizeException;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.Arrays;

public class Denoiser implements Closeable {

  private static final Logger LOGGER = LoggerFactory.getLogger(Denoiser.class);

  private static final int FRAME_SIZE = 960;

  private Pointer state;

  static {
    Native.setProtected(true);
  }

  public Denoiser() {
    LOGGER.debug("Denoiser started");
    state = Rnnoise.INSTANCE.rnnoise_create();
  }

  public void close() {
    LOGGER.debug("Denoiser closed");
    Rnnoise.INSTANCE.rnnoise_destroy(state);
    state = null;
  }

  public byte[] process(byte[] pcm) {
    int len = pcm.length;
    if (len < FRAME_SIZE) {
      return pcm;
    }

//    if (len % FRAME_SIZE != 0) {
//      throw new IllegalFrameSizeException("Size must be times of: " + FRAME_SIZE + ", len: " + len);
//    }

    byte[] denoised = new byte[len];
    int startIndex = 0;
    int endIndex = FRAME_SIZE;
    while (startIndex < len) {
      if (endIndex > len) {
        endIndex = len;
      }

      byte[] frame = Arrays.copyOfRange(pcm, startIndex, endIndex);
      byte[] processed = processFrame(frame);
      System.arraycopy(processed, 0, denoised, startIndex, processed.length);
      startIndex += FRAME_SIZE;
      endIndex += FRAME_SIZE;
    }

    return denoised;
  }

  private byte[] processFrame(byte[] frame) {
    if (frame.length == FRAME_SIZE) {
      return frame;
    }

    float[] pcmIn = Bytes.toFloatArray(frame);
    float[] pcmOut = new float[pcmIn.length];
    Rnnoise.INSTANCE.rnnoise_process_frame(state, pcmOut, pcmIn);
    return Bytes.toByteArray(pcmOut);
  }

  public interface Rnnoise extends Library {

    Rnnoise INSTANCE = (Rnnoise) Native.loadLibrary("rnnoise", Rnnoise.class);

    Pointer rnnoise_create();

    int rnnoise_get_size();

    int rnnoise_init(Pointer state);

    void rnnoise_process_frame(Pointer state, float[] out, float[] in);

    void rnnoise_destroy(Pointer stata);
  }

}
