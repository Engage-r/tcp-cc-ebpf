package org.admin.backend.service.dtos;

import lombok.Data;
import org.admin.backend.service.models.Host;

@Data
public class MachineLimit {
  private Host host;
  private Double limit;
}
