package org.admin.backend.service.exceptions;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class NotMappableException extends RuntimeException {

  private final String message = "Unable to Map Json & Object";

  private final HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
}
