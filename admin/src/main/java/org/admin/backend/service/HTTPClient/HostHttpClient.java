package org.admin.backend.service.HTTPClient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.admin.backend.service.dtos.request.HostTCPCCAlgorithmRequest;
import org.admin.backend.service.dtos.request.HostThroughputRequest;
import org.admin.backend.service.dtos.response.HostThroughputHttpResponse;
import org.admin.backend.service.exceptions.HostTCPCCAlgorithmNotSetException;
import org.admin.backend.service.exceptions.HostThroughputNotFoundException;
import org.admin.backend.service.exceptions.HostThroughputNotSetException;
import org.admin.backend.service.models.Host;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
@Slf4j
public class HostHttpClient {
  private final HttpClient httpClient;

  @Retryable(maxAttempts = 2)
  public HostThroughputHttpResponse getHostThroughput(Host host)
      throws HostThroughputNotFoundException {
    String hostURL = "http://" + host.getIp() + ":" + host.getPort();
    String url = UriComponentsBuilder.fromHttpUrl(hostURL).path("/getthroughput").toUriString();
    return httpClient.request(HttpMethod.POST, url, null, null, HostThroughputHttpResponse.class);
  }

  @Recover
  public HostThroughputHttpResponse getHostThroughput(Throwable throwable, Host host) {
    log.error("couldn't fetch throughput for host " + throwable);
    throw new HostThroughputNotFoundException();
  }

  @Retryable(maxAttempts = 2)
  public void setHostThroughput(HostThroughputRequest hostThroughputRequest)
      throws HostThroughputNotSetException {
    String url =
        UriComponentsBuilder.fromHttpUrl(hostThroughputRequest.getHostURL())
            .path("/throughput")
            .toUriString();
    httpClient.request(HttpMethod.POST, url, hostThroughputRequest, null, null);
  }

  @Recover
  public void setHostThroughput(Throwable throwable, HostThroughputRequest hostThroughputRequest) {
    log.error("host throughput not set!" + throwable);
    throw new HostThroughputNotSetException();
  }

  @Retryable(maxAttempts = 2)
  public void setHostTCPCCAlgorithm(HostTCPCCAlgorithmRequest hostTCPCCAlgorithm)
      throws HostTCPCCAlgorithmNotSetException {
    String url =
        UriComponentsBuilder.fromHttpUrl(hostTCPCCAlgorithm.getHostURL()).path("/cc").toUriString();
    httpClient.request(HttpMethod.POST, url, hostTCPCCAlgorithm, null, null);
  }

  @Recover
  public void setHostTCPCCAlgorithm(
      Throwable throwable, HostTCPCCAlgorithmRequest hostTCPCCAlgorithm) {
    log.error("host CC not set!" + throwable);
    throw new HostTCPCCAlgorithmNotSetException();
  }
}
