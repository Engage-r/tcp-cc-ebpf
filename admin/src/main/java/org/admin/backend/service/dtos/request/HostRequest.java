package org.admin.backend.service.dtos.request;

import lombok.Data;

@Data
public class HostRequest {
  private String ip;
  private Integer port;
    private Boolean isActive;
}
