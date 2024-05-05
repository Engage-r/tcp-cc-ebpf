package org.admin.backend.service.controller;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.Services.AlgorithmService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/algorithm")
public class AlgorithmController {
  private final AlgorithmService algorithmService;

  @PostMapping("/algorithmA")
  public void runAlgorithmA() {
    algorithmService.setAlgorithmToRun(1);
  }

  @PostMapping("/algorithmB")
  public void runAlgorithmB() {
    algorithmService.setAlgorithmToRun(2);
  }

  @PostMapping("/stop")
  public void stopAlgorithm() {
    algorithmService.setAlgorithmToRun(0);
  }
}
