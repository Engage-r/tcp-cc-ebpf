package org.admin.backend.service.dtos.response;

import lombok.Data;

@Data
public class HostResponse {
  private String ip;
  private Integer port;
  private Boolean isActive;
}
