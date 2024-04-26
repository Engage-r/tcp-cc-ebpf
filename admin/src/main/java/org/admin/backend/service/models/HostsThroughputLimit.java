package org.admin.backend.service.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.admin.backend.service.dtos.request.HostThroughputRequest;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@Table(name = "hosts_throughput_limit")
public class HostsThroughputLimit {
  @Id private LocalDateTime dateTime;

  @JdbcTypeCode(SqlTypes.JSON)
  List<HostThroughputRequest> hostsThroughputLimit;
}
