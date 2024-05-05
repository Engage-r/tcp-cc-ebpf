package org.admin.backend.service.repositories;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.daos.HostDao;
import org.admin.backend.service.exceptions.HostNotFoundException;
import org.admin.backend.service.models.Host;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class HostRepository {
  private final HostDao hostDao;

  public void saveAll(List<Host> hosts) {
    hostDao.saveAll(hosts);
  }

  public void save(Host host) {
    hostDao.save(host);
  }

  public List<Host> findAllHosts() {
    return hostDao.findAll();
  }

  public List<Host> findActiveHosts() {
    return hostDao.findByIsActive(Boolean.TRUE);
  }

  public Host findByIpAndPort(String ip, Long port) {
    return hostDao.findByIpAndPort(ip, port).orElseThrow(HostNotFoundException::new);
  }
  public Host findById(Long Id){
    return hostDao.findById(Id).orElseThrow(HostNotFoundException::new);
  }
}
