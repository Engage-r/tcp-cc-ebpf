package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.dtos.AlgorithmInput;
import org.admin.backend.service.mappers.HostThroughputMapper;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.admin.backend.service.repositories.HostsThroughputLimitRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AlgorithmB {
  private final HostThroughputService hostThroughputService;
  private final HostThroughputMapper hostThroughputMapper;
  private final HostsThroughputLimitRepository hostsThroughputLimitRepository;

  public List<HostThroughput> findThroughputLimits(
      List<HostThroughput> hostsRequiredThroughput,
      List<HostThroughput> hostsAverageThroughput,
      List<HostThroughput> oldHostsThroughputLimit) {
    List<AlgorithmInput> algorithmInputs =
        getAlgorithmInputs(
            hostsRequiredThroughput, hostsAverageThroughput, oldHostsThroughputLimit);
    List<HostThroughput> newHostsThroughputLimit = new ArrayList<>();
    // check if currThr>reqThr for all
    // and currThr==limit for all:
    //    then increase x% limit for all
    // else do nothing
    // currTh<reqTh
    // new limit=reqThr+(oldLimit-reqThr)/2 for all
    if (ifNetworkUnderutilized(algorithmInputs)) {
      newHostsThroughputLimit = increaseThroughputLimitOfAllHosts(algorithmInputs);
    }
    if (ifRequiredThroughputNotSatisfied(algorithmInputs)) {
      newHostsThroughputLimit = decreaseThroughputLimitOfAllHosts(algorithmInputs);
    }
    return newHostsThroughputLimit;
  }

  private Boolean ifNetworkUnderutilized(List<AlgorithmInput> algorithmInputs) {
    Double tolerance = 0.0000001;
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      if (Math.abs(algorithmInput.getCurrentThroughput() - algorithmInput.getThroughputLimit())
          > tolerance) {
        return Boolean.FALSE;
      }
    }
    return Boolean.TRUE;
  }

  private Boolean ifRequiredThroughputNotSatisfied(List<AlgorithmInput> algorithmInputs) {
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      if (algorithmInput.getCurrentThroughput() < algorithmInput.getRequiredThroughput()) {
        return Boolean.TRUE;
      }
    }
    return Boolean.FALSE;
  }

  private List<HostThroughput> decreaseThroughputLimitOfAllHosts(
      List<AlgorithmInput> algorithmInputs) {
    Double decreaseFactor = 0.02;
    List<HostThroughput> newHostsThroughputLimit = new ArrayList<>();
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      Double excessThroughput =
          algorithmInput.getThroughputLimit() - algorithmInput.getRequiredThroughput();
      Double newThroughputLimit =
          algorithmInput.getThroughputLimit() - decreaseFactor * excessThroughput;
      newHostsThroughputLimit.add(
          hostThroughputMapper.mapHostAndThroughput(algorithmInput.getHost(), newThroughputLimit));
    }
    return newHostsThroughputLimit;
  }

  private List<HostThroughput> increaseThroughputLimitOfAllHosts(
      List<AlgorithmInput> algorithmInputs) {
    Double increaseFactor = 0.03;
    List<HostThroughput> newHostsThroughputLimit = new ArrayList<>();
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      Double oldThroughputLimit = algorithmInput.getThroughputLimit();
      Double newThroughputLimit = oldThroughputLimit + oldThroughputLimit * increaseFactor;
      newHostsThroughputLimit.add(
          hostThroughputMapper.mapHostAndThroughput(algorithmInput.getHost(), newThroughputLimit));
    }
    return newHostsThroughputLimit;
  }

  public void run() {
    List<HostThroughput> requiredHostsThroughput =
        hostThroughputService.getRequiredThroughputOfAllHosts();
    List<HostThroughput> averageThroughputOfAllHosts =
        hostThroughputService.getAverageThroughputOfAllHosts();
    List<HostThroughput> oldHostsThroughputLimit =
        hostsThroughputLimitRepository.findLatestHostsThroughputLimit().getHostsThroughputLimit();
    List<HostThroughput> newHostsThroughputLimit =
        findThroughputLimits(
            requiredHostsThroughput, averageThroughputOfAllHosts, oldHostsThroughputLimit);
    hostThroughputService.setThroughputLimitOfHosts(newHostsThroughputLimit);
  }

  private List<AlgorithmInput> getAlgorithmInputs(
      List<HostThroughput> hostsRequiredThroughput,
      List<HostThroughput> hostsAverageThroughput,
      List<HostThroughput> oldHostsThroughputLimit) {

    List<AlgorithmInput> algorithmInputs = new ArrayList<>();
    Map<Host, Double> averageThroughputMap = new HashMap<>();
    Map<Host, Double> throughputLimitMap = new HashMap<>();
    for (HostThroughput hostThroughput : hostsAverageThroughput) {
      averageThroughputMap.put(hostThroughput.getHost(), hostThroughput.getThroughput());
    }
    for (HostThroughput hostThroughput : oldHostsThroughputLimit) {
      throughputLimitMap.put(hostThroughput.getHost(), hostThroughput.getThroughput());
    }
    for (HostThroughput requiredHostThroughput : hostsRequiredThroughput) {
      AlgorithmInput algorithmInput = new AlgorithmInput();
      algorithmInput.setHost(requiredHostThroughput.getHost());
      algorithmInput.setRequiredThroughput(requiredHostThroughput.getThroughput());
      algorithmInput.setCurrentThroughput(
          averageThroughputMap.get(requiredHostThroughput.getHost()));
      algorithmInput.setThroughputLimit(throughputLimitMap.get(requiredHostThroughput.getHost()));
      algorithmInputs.add(algorithmInput);
    }
    return algorithmInputs;
  }
}
