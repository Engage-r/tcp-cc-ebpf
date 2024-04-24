package org.admin.backend.service.mappers;

import org.admin.backend.service.dtos.request.HostRequest;
import org.admin.backend.service.dtos.response.HostResponse;
import org.admin.backend.service.models.Host;
import org.springframework.stereotype.Component;

@Component
public class HostMapper {
  public HostResponse mapToHostResponse(Host host) {
    HostResponse hostResponse = new HostResponse();
    hostResponse.setIp(host.getIp());
    hostResponse.setPort(host.getPort());
    hostResponse.setIsActive(host.getIsActive());
    return hostResponse;
  }

  public Host mapToHost(HostRequest hostRequest) {
    Host host = new Host();
    host.setIp(hostRequest.getIp());
    host.setPort(hostRequest.getPort());
    host.setIsActive(hostRequest.getIsActive());
    return host;
  }
}
