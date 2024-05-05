package org.admin.backend.service.mappers;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.configuration.HostConfiguration;
import org.admin.backend.service.dtos.request.HostTCPCCAlgorithmRequest;
import org.admin.backend.service.enums.TCPCCAlgorithm;
import org.admin.backend.service.models.Host;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HostTCPCCAlgorithmMapper {
  public HostTCPCCAlgorithmRequest mapHostToHostTCPCCAlgorithmRequest(
      Host host, TCPCCAlgorithm tcpccAlgorithm) {
    HostTCPCCAlgorithmRequest hostTCPCCAlgorithmRequest = new HostTCPCCAlgorithmRequest();
    hostTCPCCAlgorithmRequest.setIp(host.getIp());
    hostTCPCCAlgorithmRequest.setPort(host.getPort());
    hostTCPCCAlgorithmRequest.setTcpccAlgorithm(tcpccAlgorithm);
    return hostTCPCCAlgorithmRequest;
  }
}
