package org.admin.backend.service.dtos.response;

import lombok.Data;

@Data
public class HostResponse {
  private String ip;
  private Long port;
  private Boolean isActive;
}
