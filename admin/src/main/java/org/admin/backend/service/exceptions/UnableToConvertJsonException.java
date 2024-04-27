package org.admin.backend.service.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class UnableToConvertJsonException extends RuntimeException {
  private final HttpStatus status;
  private final String message;

  public UnableToConvertJsonException() {
    super();
    this.status = HttpStatus.UNPROCESSABLE_ENTITY;
    this.message = "Unable to JSON Convert the Object";
  }
}
