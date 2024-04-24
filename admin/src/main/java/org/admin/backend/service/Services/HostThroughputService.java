package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.admin.backend.service.HTTPClient.HostHttpClient;
import org.admin.backend.service.dtos.request.HostThroughputRequest;
import org.admin.backend.service.dtos.response.HostThroughputHttpResponse;
import org.admin.backend.service.exceptions.HostThroughputNotFoundException;
import org.admin.backend.service.exceptions.HostThroughputNotSetException;
import org.admin.backend.service.mappers.HostThroughputMapper;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.admin.backend.service.repositories.HostRepository;
import org.admin.backend.service.repositories.HostThroughputRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HostThroughputService {
  private final HostThroughputRepository hostThroughputRepository;
  private final HostHttpClient hostHttpClient;
  private final HostThroughputMapper hostThroughputMapper;
  private final HostRepository hostRepository;

  public void updateLatestThroughputOfAllHosts() {
    List<Host> activeHosts = hostRepository.findActiveHosts();
    List<HostThroughput> activeHostThroughputs = new ArrayList<>();
    for (Host host : activeHosts) {
      try {

        HostThroughputHttpResponse hostThroughputResponse = hostHttpClient.getHostThroughput(host);
        HostThroughput hostThroughput =
            hostThroughputMapper.mapHostAndHostThroughputResponse(host, hostThroughputResponse);
        activeHostThroughputs.add(hostThroughput);
      } catch (HostThroughputNotFoundException e) {
        log.error("couldn't get throughput for host: " + host);
      }
    }
    hostThroughputRepository.saveAll(activeHostThroughputs);
  }

  public void setThroughputOfHost(HostThroughputRequest hostThroughputRequest) {
    Host host =
        hostRepository.findByIpAndPort(
            hostThroughputRequest.getIp(), hostThroughputRequest.getPort());
    if (host.getIsActive() == Boolean.FALSE) throw new HostThroughputNotSetException();
    try {
      hostHttpClient.setHostThroughput(hostThroughputRequest);
    } catch (HostThroughputNotSetException e) {
      log.error("Couldn't set throughput for host:" + host);
    }
  }
}
