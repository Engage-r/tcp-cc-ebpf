package org.admin.backend.service.Services;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.configuration.HostConfiguration;
import org.admin.backend.service.dtos.AlgorithmInput;
import org.admin.backend.service.enums.Priority;
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
  private final HostConfiguration hostConfiguration;

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
    // complex logic
    if (ifNetworkUnderutilized(algorithmInputs)) {
      newHostsThroughputLimit = increaseThroughputLimitOfAllHosts(algorithmInputs);
    } else if (ifRequiredThroughputNotSatisfied(algorithmInputs)) {
      newHostsThroughputLimit = decreaseThroughputLimitOfHigherPriorityHosts(algorithmInputs);
    }
    return newHostsThroughputLimit;
  }

  private Boolean ifNetworkUnderutilized(List<AlgorithmInput> algorithmInputs) {
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      Double tolerance = algorithmInput.getThroughputLimit() * 0.1;
      //      if (Math.abs(algorithmInput.getCurrentThroughput() -
      // algorithmInput.getThroughputLimit())
      //          > tolerance) {
      //        return Boolean.FALSE;
      //      }
      if (algorithmInput.getCurrentThroughput() < algorithmInput.getThroughputLimit()) {
        if ((algorithmInput.getThroughputLimit() - algorithmInput.getCurrentThroughput())
            > tolerance) {
          return Boolean.FALSE;
        }
      }
    }
    return Boolean.TRUE;
  }

  private Boolean ifRequiredThroughputNotSatisfied(List<AlgorithmInput> algorithmInputs) {
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      Double tolerance = algorithmInput.getRequiredThroughput() * 0.1;
      if (algorithmInput.getCurrentThroughput() < algorithmInput.getRequiredThroughput()) {
        if ((algorithmInput.getRequiredThroughput() - algorithmInput.getCurrentThroughput())
            > tolerance) {
          return Boolean.TRUE;
        }
      }
    }
    return Boolean.FALSE;
  }

  //  private List<HostThroughput> decreaseThroughputLimitOfAllHosts(
  //      List<AlgorithmInput> algorithmInputs) {
  //    Double decreaseFactor = 0.02;
  //    List<HostThroughput> newHostsThroughputLimit = new ArrayList<>();
  //    for (AlgorithmInput algorithmInput : algorithmInputs) {
  //      Double excessThroughput =
  //          algorithmInput.getThroughputLimit() - algorithmInput.getRequiredThroughput();
  //      Double newThroughputLimit =
  //          algorithmInput.getThroughputLimit() - decreaseFactor * excessThroughput;
  //      newHostsThroughputLimit.add(
  //          hostThroughputMapper.mapHostAndThroughput(algorithmInput.getHost(),
  // newThroughputLimit));
  //    }
  //    return newHostsThroughputLimit;
  //  }
  private List<HostThroughput> decreaseThroughputLimitOfHigherPriorityHosts(
      List<AlgorithmInput> algorithmInputs) {
    Double requiredThroughputSum = getRequiredThroughputSum(algorithmInputs);
    // set limit for aggressive algo
    Double newThroughputLimit =
        doBinarySearch(
            hostConfiguration.getRequiredThroughput(Priority.LEVEL_1),
            hostConfiguration.getRequiredThroughput(Priority.LEVEL_0),
            requiredThroughputSum,
            algorithmInputs);
    List<HostThroughput> newHostsThroughputLimit = new ArrayList<>();
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      if (algorithmInput.getHost().getPriority() == Priority.LEVEL_0) {
        newHostsThroughputLimit.add(
            hostThroughputMapper.mapHostAndThroughput(
                algorithmInput.getHost(), newThroughputLimit));
      } else
        newHostsThroughputLimit.add(
            (hostThroughputMapper.mapHostAndThroughput(
                algorithmInput.getHost(), algorithmInput.getThroughputLimit())));
    }
    return newHostsThroughputLimit;
  }

  private double doBinarySearch(
      double lowest, double highest, double requiredValue, List<AlgorithmInput> algorithmInputs) {
    double mid = 0.0;
    double answer = lowest;
    while (lowest <= highest) {
      mid = (lowest + highest) / 2;
      double extractedThroughput = 0.0;
      for (AlgorithmInput algorithmInput : algorithmInputs) {
        if (algorithmInput.getHost().getPriority() == Priority.LEVEL_0) {
          Double upperTolerance = mid * 0.1;
          if (algorithmInput.getCurrentThroughput() > mid) {
            if ((algorithmInput.getCurrentThroughput() - mid) > upperTolerance) {
              extractedThroughput += algorithmInput.getCurrentThroughput() - mid;
            }
          }
        }
      }
      if (extractedThroughput >= requiredValue) {
        answer = mid;
        lowest = mid + 1;
      } else highest = mid - 1;
    }
    return answer;
  }

  private Double getRequiredThroughputSum(List<AlgorithmInput> algorithmInputs) {
    Double requiredThroughputSum = 0.0;
    // TODO: handle case if currentThroughput of LEVEL_0 algo falls below LEVEL_1 required
    // throughput
    for (AlgorithmInput algorithmInput : algorithmInputs) {
      if (algorithmInput.getHost().getPriority() == Priority.LEVEL_1) {
        Double tolerance = algorithmInput.getRequiredThroughput() * 0.1;
        if (algorithmInput.getCurrentThroughput() < algorithmInput.getRequiredThroughput()) {
          if ((algorithmInput.getRequiredThroughput() - algorithmInput.getCurrentThroughput())
              > tolerance) {
            requiredThroughputSum +=
                (algorithmInput.getRequiredThroughput() - algorithmInput.getCurrentThroughput())
                    * 0.95;
          }
        }
      }
    }
    return requiredThroughputSum;
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
