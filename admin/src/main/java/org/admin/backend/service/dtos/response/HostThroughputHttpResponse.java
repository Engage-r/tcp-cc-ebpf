package org.admin.backend.service.dtos.response;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class HostThroughputHttpResponse {
  private Double throughput;
  private LocalDate date;
  private LocalTime time;
}
