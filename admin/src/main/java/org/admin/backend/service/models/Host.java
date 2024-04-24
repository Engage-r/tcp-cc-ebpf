package org.admin.backend.service.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;

@Entity
@Data
public class Host {

  @GeneratedValue(strategy = GenerationType.AUTO)
  @Id
  private Long id;

  private String ip;
  private Integer port;
  private Boolean isActive;

  public String getHostURL() {
    return "http://" + ip + ":" + port;
  }
}
