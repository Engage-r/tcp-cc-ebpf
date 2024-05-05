package org.admin.backend.service.controller;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.Services.HostThroughputService;
import org.admin.backend.service.dtos.request.HostThroughputRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hostThroughput")
public class HostThroughputController {
  private final HostThroughputService hostThroughputService;

  @PutMapping("/updateAll")
  public void updateThroughputOfAllHosts() {
    hostThroughputService.updateLatestThroughputOfAllHosts();
  }

  @PostMapping("/set")
  public void setThroughputOfHost(@RequestBody HostThroughputRequest hostThroughputRequest) {
    hostThroughputService.setThroughputLimitOfHost(hostThroughputRequest);
  }
}
