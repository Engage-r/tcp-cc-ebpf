package org.admin.backend.service.mappers;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.configuration.HostConfiguration;
import org.admin.backend.service.dtos.request.HostTCPCCAlgorithmRequest;
import org.admin.backend.service.models.Host;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HostTCPCCAlgorithmMapper {
  private final HostConfiguration hostConfiguration;

  public HostTCPCCAlgorithmRequest mapHostToHostTCPCCAlgorithmRequest(Host host) {
    HostTCPCCAlgorithmRequest hostTCPCCAlgorithmRequest = new HostTCPCCAlgorithmRequest();
    hostTCPCCAlgorithmRequest.setIp(host.getIp());
    hostTCPCCAlgorithmRequest.setPort(host.getPort());
    hostTCPCCAlgorithmRequest.setTcpccAlgorithm(
        hostConfiguration.getTCPCCAlgorithm(host.getPriority()));
    return hostTCPCCAlgorithmRequest;
  }
}
