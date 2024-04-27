package org.admin.backend.service.dtos;

import lombok.Data;
import org.admin.backend.service.models.Host;

@Data
public class Machine {
  private Host host;
  private Double currentThroughput;
  private Double requiredThroughput;
}
