package org.admin.backend.service.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.admin.backend.service.exceptions.NotMappableException;
import org.admin.backend.service.exceptions.UnableToConvertJsonException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MapperUtil {

  private final ObjectMapper objectMapper;

  public MapperUtil(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  public <T> T convert(Object object, Class<T> toClassType) {
    return objectMapper.convertValue(object, toClassType);
  }

  public <T> T convert(Object object, TypeReference<T> toClassType) {
    return objectMapper.convertValue(object, toClassType);
  }

  public String writeValueAsString(Object object) throws NotMappableException {
    try {
      return objectMapper.writeValueAsString(object);
    } catch (JsonProcessingException e) {
      log.error("Error Converting Object to String, exception - ", e);
      throw new NotMappableException();
    }
  }

  public <T> T convertValue(Object fromValue, Class<T> toValueType)
      throws UnableToConvertJsonException {
    try {
      return objectMapper.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      log.error("Error converting object : {} to class:{}", fromValue, toValueType, e);
      throw new UnableToConvertJsonException();
    }
  }

  public <T> T convertValue(Object fromValue, TypeReference<T> toValueType)
      throws UnableToConvertJsonException {
    try {
      return objectMapper.convertValue(fromValue, toValueType);
    } catch (IllegalArgumentException e) {
      log.error("Error converting object : {} to class:{}", fromValue, toValueType, e);
      throw new UnableToConvertJsonException();
    }
  }

  public <T> T convertValue(String content, Class<T> valueType) throws NotMappableException {

    try {
      return objectMapper.readValue(content, valueType);
    } catch (JsonProcessingException e) {
      log.error("Error Converting Object to String, exception - ", e);
      throw new NotMappableException();
    }
  }

  public <T> T readValue(String content, Class<T> valueType) throws NotMappableException {

    try {
      return objectMapper.readValue(content, valueType);
    } catch (JsonProcessingException e) {
      log.error(
          "Error Converting String to Object of {}, exception - {}", valueType, e.getStackTrace());
      throw new NotMappableException();
    }
  }

  public <T> T readValue(String content, TypeReference<T> toValueType) throws NotMappableException {
    try {
      return objectMapper.readValue(content, toValueType);
    } catch (JsonProcessingException e) {
      log.error(
          "Error Converting String to Object of {}, exception - {}",
          toValueType,
          e.getStackTrace());
      throw new NotMappableException();
    }
  }
}
