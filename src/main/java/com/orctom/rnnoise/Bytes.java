package com.orctom.rnnoise;

public abstract class Bytes {

  public static byte[] toByteArray(float[] input) {
    byte[] ret = new byte[input.length * 2];
    for (int i = 0; i < input.length; ++i) {
      short x = ((Float) input[i]).shortValue();
      ret[i * 2] = (byte) (x & 0x00FF);
      ret[i * 2 + 1] = (byte) ((x & 0xFF00) >> 8);
    }
    return ret;
  }

  public static float[] toFloatArray(byte[] input) {
    float[] ret = new float[input.length / 2];
    for (int i = 0; i < input.length / 2; ++i) {
      if ((input[i * 2 + 1] & 0x80) != 0) {
        ret[i] = -32768 + ((input[i * 2 + 1] & 0x7f) << 8) | (input[i * 2] & 0xff);
      } else {
        ret[i] = ((input[i * 2 + 1] << 8) & 0xff00) | (input[i * 2] & 0xff);
      }
    }
    return ret;
  }
}
