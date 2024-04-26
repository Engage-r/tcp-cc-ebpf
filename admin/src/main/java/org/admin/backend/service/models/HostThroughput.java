package org.admin.backend.service.models;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Data
@IdClass(HostThroughput.HostThroughputId.class)
@Table(name = "host_throughput")
public class HostThroughput {
  @Id @ManyToOne private Host host;
  @Id private LocalDateTime dateTime;
  private Double throughput;

  public static class HostThroughputId implements Serializable {
    @ManyToOne @Id private Host host;
    @Id private LocalDateTime dateTime;
  }
}
