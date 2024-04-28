package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.admin.backend.service.HTTPClient.HostHttpClient;
import org.admin.backend.service.configuration.HostConfiguration;
import org.admin.backend.service.dtos.request.HostRequest;
import org.admin.backend.service.dtos.request.HostTCPCCAlgorithmRequest;
import org.admin.backend.service.dtos.response.HostResponse;
import org.admin.backend.service.exceptions.HostTCPCCAlgorithmNotSetException;
import org.admin.backend.service.mappers.HostMapper;
import org.admin.backend.service.mappers.HostTCPCCAlgorithmMapper;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.repositories.HostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HostService {
  private final HostRepository hostRepository;
  private final HostMapper hostMapper;
  private final HostHttpClient hostHttpClient;
  private final HostTCPCCAlgorithmMapper hostTCPCCAlgorithmMapper;
  private final HostConfiguration hostConfiguration;

  public List<HostResponse> getAllHosts() {
    List<Host> hosts = hostRepository.findAllHosts();
    List<HostResponse> hostResponses = new ArrayList<>();
    for (Host host : hosts) {
      hostResponses.add(hostMapper.mapToHostResponse(host));
    }
    return hostResponses;
  }

  public List<HostResponse> getAllActiveHosts() {
    List<Host> hosts = hostRepository.findActiveHosts();
    List<HostResponse> hostResponses = new ArrayList<>();
    for (Host host : hosts) {
      hostResponses.add(hostMapper.mapToHostResponse(host));
    }
    return hostResponses;
  }

  public void createNewHost(HostRequest hostRequest) {
    hostRepository.save(hostMapper.mapToHost(hostRequest));
  }

  public void setTCPCCAlgorithmForHost(HostTCPCCAlgorithmRequest hostTCPCCAlgorithmRequest) {
    try {
      hostHttpClient.setHostTCPCCAlgorithm(hostTCPCCAlgorithmRequest);
    } catch (HostTCPCCAlgorithmNotSetException e) {
      log.error("TCP algo not set for host!" + e);
    }
  }

  public void setTCPCCAlgorithmForAllActiveHosts() {
    List<Host> activeHosts = hostRepository.findActiveHosts();
    for (Host host : activeHosts) {
      HostTCPCCAlgorithmRequest hostTCPCCAlgorithmRequest =
          hostTCPCCAlgorithmMapper.mapHostToHostTCPCCAlgorithmRequest(
              host, hostConfiguration.getTCPCCAlgorithm(host.getPriority()));
      setTCPCCAlgorithmForHost(hostTCPCCAlgorithmRequest);
    }
  }
}
