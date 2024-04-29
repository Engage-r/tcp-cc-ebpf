package org.admin.backend.service.configuration;

import org.admin.backend.service.enums.Priority;
import org.admin.backend.service.enums.TCPCCAlgorithm;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HostConfiguration {

  public Double getRequiredThroughput(Priority priority) {
    Double requiredThroughput = 0.0;
    switch (priority) {
      case LEVEL_0 -> requiredThroughput = 10000.0;
      case LEVEL_1 -> requiredThroughput = 8000.0;
      case LEVEL_2 -> requiredThroughput = 5000.0;
    }
    return requiredThroughput;
  }

  public TCPCCAlgorithm getTCPCCAlgorithm(Priority priority) {
    TCPCCAlgorithm tcpccAlgorithm = TCPCCAlgorithm.VEGAS;
    switch (priority) {
      case LEVEL_0 -> tcpccAlgorithm = TCPCCAlgorithm.CUBIC;
    }
    return tcpccAlgorithm;
  }
}
