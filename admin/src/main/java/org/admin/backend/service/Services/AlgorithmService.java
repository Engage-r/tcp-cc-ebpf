package org.admin.backend.service.Services;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class AlgorithmService {
  private final AlgorithmA algorithmA;
  private final AlgorithmB algorithmB;
  private Integer algorithmToRun = 0;

  @Scheduled(fixedDelayString = "${algorithmFixedDelay.in.milliseconds}")
  public void runAlgorithmA() {
    if (algorithmToRun == 1) algorithmA.run();
  }

  @Scheduled(fixedDelayString = "${algorithmFixedDelay.in.milliseconds}")
  public void runAlgorithmB() {
    if (algorithmToRun == 2) algorithmB.run();
  }
}
