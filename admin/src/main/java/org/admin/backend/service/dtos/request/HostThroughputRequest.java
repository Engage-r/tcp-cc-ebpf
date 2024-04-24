package org.admin.backend.service.dtos.request;

import lombok.Data;

@Data
public class HostThroughputRequest {
  private String ip;
  private Long port;
  private Double throughput;

  public String getHostURL() {
    return "http://" + ip + ":" + port;
  }
}
