package org.admin.backend.service.dtos.request;

import lombok.Data;
import org.admin.enums.TCPCCAlgorithm;

@Data
public class HostTCPCCAlgorithmRequest {
  private String ip;
  private Long port;
  private TCPCCAlgorithm tcpccAlgorithm;

  public String getHostURL() {
    return "http://" + ip + ":" + port;
  }
}
