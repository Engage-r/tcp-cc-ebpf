package org.admin.backend.service.models;

import jakarta.persistence.*;
import lombok.Data;
import org.admin.enums.Priority;

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

  public String getHostURL() {
    return "http://" + ip + ":" + port;
  }
}
