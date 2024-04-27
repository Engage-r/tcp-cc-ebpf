package org.admin.backend.service.dtos.request;

import lombok.Data;

@Data
public class HostRequest {
  private String ip;
  private Long port;
  private Boolean isActive;
}
