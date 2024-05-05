package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.admin.backend.service.HTTPClient.HostHttpClient;
import org.admin.backend.service.configuration.HostConfiguration;
import org.admin.backend.service.dtos.request.HostThroughputRequest;
import org.admin.backend.service.dtos.response.HostThroughputHttpResponse;
import org.admin.backend.service.exceptions.HostThroughputNotFoundException;
import org.admin.backend.service.exceptions.HostThroughputNotSetException;
import org.admin.backend.service.mappers.HostThroughputMapper;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.admin.backend.service.models.HostsThroughputLimit;
import org.admin.backend.service.repositories.HostRepository;
import org.admin.backend.service.repositories.HostThroughputRepository;
import org.admin.backend.service.repositories.HostsThroughputLimitRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
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
  private final HostsThroughputLimitRepository hostsThroughputLimitRepository;
  private final HostConfiguration hostConfiguration;

  @Scheduled(fixedDelayString = "${updateThroughputFixedDelay.in.milliseconds}")
  @Transactional
  public void updateLatestThroughputOfAllHosts() {
    List<Host> activeHosts = hostRepository.findActiveHosts();
    List<HostThroughput> activeHostsThroughput = new ArrayList<>();
    for (Host host : activeHosts) {
      try {

        HostThroughputHttpResponse hostThroughputResponse = hostHttpClient.getHostThroughput(host);
        HostThroughput hostThroughput =
            hostThroughputMapper.mapHostAndHostThroughputResponse(host, hostThroughputResponse);
        activeHostsThroughput.add(hostThroughput);
      } catch (HostThroughputNotFoundException e) {
        log.error("couldn't get throughput for host: " + host);
      }
    }
    hostThroughputRepository.saveAll(activeHostsThroughput);
  }

  public void setThroughputLimitOfHost(HostThroughputRequest hostThroughputLimitRequest)
      throws HostThroughputNotSetException {
    Host host =
        hostRepository.findByIpAndPort(
            hostThroughputLimitRequest.getIp(), hostThroughputLimitRequest.getPort());
    if (host.getIsActive() == Boolean.FALSE) throw new HostThroughputNotSetException();
    try {
      hostHttpClient.setHostThroughput(hostThroughputLimitRequest);
    } catch (HostThroughputNotSetException e) {
      log.error("Couldn't set throughput for host:" + host);
      throw new HostThroughputNotSetException();
    }
  }

  @Transactional
  public void setThroughputLimitOfHosts(List<HostThroughput> hostsThroughput) {
    if (hostsThroughput.isEmpty()) return;
    try {
      for (HostThroughput hostThroughput : hostsThroughput) {
        setThroughputLimitOfHost(
            hostThroughputMapper.mapToHostThroughputHttpRequest(hostThroughput));
      }
    } catch (HostThroughputNotSetException e) {
      log.error("cancelling further throughput limit requests!");
    }
    HostsThroughputLimit hostsThroughputLimit = new HostsThroughputLimit();
    hostsThroughputLimit.setHostsThroughputLimit(hostsThroughput);
    hostsThroughputLimit.setDateTime(LocalDateTime.now());
    hostsThroughputLimitRepository.save(hostsThroughputLimit);
  }

  public List<HostThroughput> getAverageThroughputOfAllHosts() {
    // get avg throughput of active hosts from last updation
    HostsThroughputLimit latestHostsThroughputLimit =
        hostsThroughputLimitRepository.findLatestHostsThroughputLimit();
    List<HostThroughput> averageHostsThroughput =
        hostThroughputRepository.findAverageThroughputOfActiveHostsAfterTime(
            latestHostsThroughputLimit.getDateTime());
    // TODO: check if all active hosts fetched or not
    return averageHostsThroughput;
  }

  public List<HostThroughput> getRequiredThroughputOfAllHosts() {
    List<Host> hosts = hostRepository.findActiveHosts();
    List<HostThroughput> hostsThroughput = new ArrayList<>();
    for (Host host : hosts) {
      HostThroughput hostThroughput =
          hostThroughputMapper.mapHostAndThroughput(
              host, hostConfiguration.getRequiredThroughput(host.getPriority()));
      hostsThroughput.add(hostThroughput);
    }
    return hostsThroughput;
  }
}
