package org.admin.backend.service.HTTPClient;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.admin.backend.service.utils.MapperUtil;
import org.admin.backend.service.utils.RestClientUtil;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;

@Component
@Slf4j
@RequiredArgsConstructor
public class HttpClient {

  private final MapperUtil mapperUtil;

  public <A, B> B request(
      HttpMethod httpMethod,
      String completeUrl,
      A body,
      HttpHeaders httpHeaders,
      Class<B> responseType) {
    ResponseEntity<B> responseEntity = null;
    try {
      log.info(
          "Request URL - {}, Request Body - {}, Headers - {}",
          completeUrl,
          mapperUtil.writeValueAsString(body),
          httpHeaders);
      RequestEntity<A> requestEntity;

      requestEntity = new RequestEntity<>(body, httpHeaders, httpMethod, new URI(completeUrl));
      long startTime = System.currentTimeMillis();
      responseEntity =
          RestClientUtil.getRestTemplate(15000, 5000).exchange(requestEntity, responseType);

      log.info(
          "Response Entity - {}, and time take by api - {}",
          responseEntity,
          (System.currentTimeMillis() - startTime));
      log.info("Response JSON - {}", mapperUtil.writeValueAsString(responseEntity.getBody()));
    } catch (HttpStatusCodeException httpEx) {
      log.error(
          "Http Status Code Error - {} - {}",
          httpEx.getStatusCode(),
          httpEx.getResponseBodyAsString());
      throw httpEx;
    } catch (URISyntaxException uriEx) {
      log.error("Error in Parsing URI - {}", uriEx.getMessage());
    } catch (Exception ex) {
      log.error("Error Encountered - {}", ex.getMessage());
      throw ex;
    }

    return Objects.requireNonNull(responseEntity).getBody();
  }
}
