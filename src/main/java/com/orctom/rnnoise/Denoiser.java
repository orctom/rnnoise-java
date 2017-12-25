package com.orctom.rnnoise;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;

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

  public byte[] process(byte[] dataIn) {
    if (dataIn.length < FRAME_SIZE) {
      return dataIn;
    }
    float[] pcmIn = Bytes.toFloatArray(dataIn);
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
