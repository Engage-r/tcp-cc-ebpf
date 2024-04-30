package org.admin.backend.service.mappers;

import org.admin.backend.service.dtos.request.HostThroughputRequest;
import org.admin.backend.service.dtos.response.HostThroughputHttpResponse;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class HostThroughputMapper {
  public HostThroughput mapHostAndHostThroughputResponse(
      Host host, HostThroughputHttpResponse hostThroughputResponse) {
    HostThroughput hostThroughput = new HostThroughput();
    hostThroughput.setHost(host);
    hostThroughput.setThroughput(hostThroughputResponse.getThroughput());
    LocalDateTime dateTime =
        LocalDateTime.of(hostThroughputResponse.getDate(), hostThroughputResponse.getTime());
    hostThroughput.setDateTime(dateTime);
    return hostThroughput;
  }

  public HostThroughputRequest mapToHostThroughputHttpRequest(HostThroughput hostThroughput) {
    HostThroughputRequest hostThroughputRequest = new HostThroughputRequest();
    hostThroughputRequest.setIp(hostThroughput.getHost().getIp());
    hostThroughputRequest.setPort(hostThroughput.getHost().getPort());
    hostThroughputRequest.setThroughput(hostThroughput.getThroughput());
    return hostThroughputRequest;
  }

  public HostThroughput mapHostAndThroughput(Host host, Double throughput) {
    HostThroughput hostThroughput = new HostThroughput();
    hostThroughput.setHost(host);
    hostThroughput.setThroughput(throughput);
    return hostThroughput;
  }
}
