package com.orctom.rnnoise;

import com.google.common.base.Stopwatch;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class BytesTest {

  @Test
  public void test() {
    String text = "hello world";
    byte[] bytes = text.getBytes();
    float[] floats2 = Bytes.toFloatArray(bytes);
    float[] floats3 = Bytes.toFloatArray(Bytes.toShortArray(bytes));
    System.out.println(Arrays.toString(bytes));
    System.out.println(Arrays.toString(floats2));
    System.out.println(Arrays.toString(floats3));

    Stopwatch sw = Stopwatch.createStarted();
    short[] shorts = Bytes.toShortArray(floats2);
    System.out.println(sw.toString());

    sw.reset().start();
    System.out.println(Arrays.toString(shorts));
    System.out.println(sw.toString());

    sw.reset().start();
    System.out.println(Arrays.toString(Bytes.toByteArray(shorts)));
    System.out.println(sw.toString());


  }
}