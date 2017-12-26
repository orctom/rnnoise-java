package com.orctom.rnnoise.exception;

public class IllegalFrameSizeException extends RuntimeException {

  public IllegalFrameSizeException(String message) {
    super(message);
  }

  public IllegalFrameSizeException(String message, Throwable cause) {
    super(message, cause);
  }
}
