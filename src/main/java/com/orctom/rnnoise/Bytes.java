package com.orctom.rnnoise;

import java.nio.ByteBuffer;

/**
 * 1 byte: byte, boolean
 * 2 bytes: short, char
 * 4 bytes: int, float
 * 8 bytes:  long, double
 */
public abstract class Bytes {

  // =========================== to byte array =================================

  public static byte[] toByteArray(float[] input) {
    byte[] ret = new byte[input.length * 2];
    for (int i = 0; i < input.length; ++i) {
      short x = ((Float) input[i]).shortValue();
      ret[i * 2] = (byte) (x & 0x00FF);
      ret[i * 2 + 1] = (byte) ((x & 0xFF00) >> 8);
    }
    return ret;
  }

  public static byte[] toByteArray(short[] shorts) {
    byte[] ret = new byte[shorts.length * 2];
    for (int i = 0; i < shorts.length; ++i) {
      short x = shorts[i];
      ret[i * 2] = (byte) (x & 0x00FF);
      ret[i * 2 + 1] = (byte) ((x & 0xFF00) >> 8);
    }
    return ret;
  }

  // =========================== to short array =================================

  public static short byteToShort(byte[] b) {
    short s = 0;
    short s0 = (short) (b[0] & 0xff);
    short s1 = (short) (b[1] & 0xff);
    s1 <<= 8;
    s = (short) (s0 | s1);
    return s;
  }

  public static short[] toShortArray(byte[] bytes) {
    short[] out = new short[bytes.length / 2]; // will drop last byte if odd number
    ByteBuffer bb = ByteBuffer.wrap(bytes);
    for (int i = 0; i < out.length; i++) {
      out[i] = bb.getShort();
    }
    return out;
  }

  public static short[] toShortArray2(byte[] bytes) {
    short[] shorts = new short[bytes.length / 2];
    for (int i = 0; i < bytes.length / 2; ++i) {
      shorts[i] = (short) ((bytes[i * 2 + 1] << 8) & 0xFFFF | (bytes[i * 2] & 0x00FF));
    }
    return shorts;
  }

  public static short[] byteArray2ShortArray(byte[] b) {
    int len = b.length / 2;
    int index = 0;
    short[] re = new short[len];
    byte[] buf = new byte[2];
    for (int i = 0; i < b.length; ) {
      buf[0] = b[i];
      buf[1] = b[i + 1];
      short st = byteToShort(buf);
      re[index] = st;
      index++;
      i += 2;
    }
    return re;
  }

  public static short[] toShortArray(float[] floats) {
    short[] shorts = new short[floats.length];
    for (int i = 0; i < floats.length; i++) {
      shorts[i] = ((Float) floats[i]).shortValue();
    }
    return shorts;
  }


  // =========================== to float array =================================

  public static float[] toFloatArray(short[] shorts) {
    float[] floats = new float[shorts.length];
    for (int i = 0; i < shorts.length; i++) {
      floats[i] = shorts[i];
    }
    return floats;
  }

  public static float[] toFloatArray(byte[] bytes) {
    float[] floats = new float[bytes.length / 2];
    for (int i = 0; i < bytes.length / 2; ++i) {
      floats[i] = (float) ((bytes[i * 2 + 1] << 8) & 0xFFFF | (bytes[i * 2] & 0x00FF));
    }
    return floats;
  }

  public static float[] toFloatArray2(byte[] input) {
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
