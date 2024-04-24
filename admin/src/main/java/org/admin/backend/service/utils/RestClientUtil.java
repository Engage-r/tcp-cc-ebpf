package org.admin.backend.service.utils;

import java.time.Duration;
import java.util.Arrays;
import lombok.experimental.UtilityClass;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

@UtilityClass
public class RestClientUtil {

  public static RestTemplate getRestTemplate(int readTimeout, int connectionTimeout) {
    return (new RestTemplateBuilder())
        .setConnectTimeout(Duration.ofMillis(connectionTimeout))
        .setReadTimeout(Duration.ofMillis(readTimeout))
        .build();
  }

  public static boolean contains(final int[] arr, final int key) {
    return Arrays.stream(arr)
        .anyMatch(
            (i) -> {
              return i == key;
            });
  }
}
