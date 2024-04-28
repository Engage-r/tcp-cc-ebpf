package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.dtos.AlgorithmInput;
import org.admin.backend.service.mappers.HostThroughputMapper;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AlgorithmA {
  private final HostThroughputService hostThroughputService;
  private final HostThroughputMapper hostThroughputMapper;

  public List<HostThroughput> findThroughputLimits(
      List<HostThroughput> hostsRequiredThroughput, List<HostThroughput> hostsAverageThroughput) {
    List<AlgorithmInput> algorithmInputs =
        getAlgorithmInputs(hostsRequiredThroughput, hostsAverageThroughput);
    List<HostThroughput> limits = new ArrayList<>();
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      if (algorithmInput.getCurrentThroughput() > algorithmInput.getRequiredThroughput()) {
        double ratio =
            algorithmInput.getCurrentThroughput() / algorithmInput.getRequiredThroughput();
        double limit =
            algorithmInput.getRequiredThroughput()
                + (algorithmInput.getCurrentThroughput() - algorithmInput.getRequiredThroughput())
                    / (1 + Math.exp(0.3 * (ratio - 1)));
        limits.add(hostThroughputMapper.mapHostAndThroughput(algorithmInput.getHost(), limit));
      }
    }
    return limits;
  }

  public void run() {
    List<HostThroughput> requiredHostsThroughput =
        hostThroughputService.getRequiredThroughputOfAllHosts();
    List<HostThroughput> averageThroughputOfAllHosts =
        hostThroughputService.getAverageThroughputOfAllHosts();
    List<HostThroughput> hostsThroughputLimit =
        findThroughputLimits(requiredHostsThroughput, averageThroughputOfAllHosts);
    hostThroughputService.setThroughputLimitOfHosts(hostsThroughputLimit);
  }

  private List<AlgorithmInput> getAlgorithmInputs(
      List<HostThroughput> hostsRequiredThroughput, List<HostThroughput> hostsAverageThroughput) {

    List<AlgorithmInput> algorithmInputs = new ArrayList<>();
    Map<Host, Double> averageThroughputMap = new HashMap<>();

    for (HostThroughput hostThroughput : hostsAverageThroughput) {
      averageThroughputMap.put(hostThroughput.getHost(), hostThroughput.getThroughput());
    }

    for (HostThroughput requiredHostThroughput : hostsRequiredThroughput) {
      AlgorithmInput algorithmInput = new AlgorithmInput();
      algorithmInput.setHost(requiredHostThroughput.getHost());
      algorithmInput.setRequiredThroughput(requiredHostThroughput.getThroughput());
      algorithmInput.setCurrentThroughput(
          averageThroughputMap.get(requiredHostThroughput.getHost()));
      algorithmInputs.add(algorithmInput);
    }
    return algorithmInputs;
  }
}
