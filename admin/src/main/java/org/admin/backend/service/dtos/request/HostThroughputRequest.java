package org.admin.backend.service.dtos.request;

import lombok.Data;

import java.io.Serializable;

@Data
public class HostThroughputRequest implements Serializable {
  private String ip;
  private Long port;
  private Double throughput;

  public String getHostURL() {
    return "http://" + ip + ":" + port;
  }
}
