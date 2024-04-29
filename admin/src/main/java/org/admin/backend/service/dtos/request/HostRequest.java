package org.admin.backend.service.dtos.request;

import lombok.Data;
import org.admin.backend.service.enums.Priority;

@Data
public class HostRequest {
  private String ip;
  private Long port;
  private Boolean isActive;
  private Priority priority;
}
