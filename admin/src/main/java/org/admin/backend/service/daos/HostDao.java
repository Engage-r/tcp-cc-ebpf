package org.admin.backend.service.daos;

import org.admin.backend.service.models.Host;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface HostDao extends CrudRepository<Host, Long> {

  List<Host> findAll();

  List<Host> findByIsActive(Boolean isActive);

  Optional<Host> findByIpAndPort(String ip, Long port);
}
