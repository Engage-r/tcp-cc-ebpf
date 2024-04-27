package org.admin.backend.service.controller;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.Services.HostService;
import org.admin.backend.service.dtos.request.HostRequest;
import org.admin.backend.service.dtos.request.HostTCPCCAlgorithmRequest;
import org.admin.backend.service.dtos.response.HostResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/host")
@RequiredArgsConstructor
public class HostController {
  private final HostService hostService;

  @GetMapping("/getAll")
  public List<HostResponse> getAllHosts() {
    return hostService.getAllHosts();
  }

  @GetMapping("/getActive")
  public List<HostResponse> getActiveHosts() {
    return hostService.getAllActiveHosts();
  }

  @PostMapping("/insert")
  public void insertNewHost(@RequestBody HostRequest hostRequest) {
    hostService.createNewHost(hostRequest);
  }

  @PostMapping("/autoSetCCAll")
  public void autoSetTCPCCAlgorithmForAllActiveHosts() {
    hostService.setTCPCCAlgorithmForAllActiveHosts();
  }

  @PostMapping("/setCC")
  public void setTCPCCAlgorithmForHost(
      @RequestBody HostTCPCCAlgorithmRequest hostTCPCCAlgorithmRequest) {
    hostService.setTCPCCAlgorithmForHost(hostTCPCCAlgorithmRequest);
  }
}
