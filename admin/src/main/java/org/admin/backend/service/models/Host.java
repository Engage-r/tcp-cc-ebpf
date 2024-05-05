package org.admin.backend.service.models;

import jakarta.persistence.*;
import lombok.Data;
import org.admin.backend.service.enums.Priority;

import java.util.Objects;

@Entity
@Data
public class Host {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Long id;

  private String ip;
  private Long port;
  private Boolean isActive;
  private Priority priority;


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Host host = (Host) o;
    return Objects.equals(ip, host.ip) && Objects.equals(port, host.port);
  }

  @Override
  public int hashCode() {
    return Objects.hash(ip, port);
  }
}
