package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.dtos.request.HostRequest;
import org.admin.backend.service.dtos.response.HostResponse;
import org.admin.backend.service.mappers.HostMapper;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.repositories.HostRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HostService {
  private final HostRepository hostRepository;
  private final HostMapper hostMapper;

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
}
