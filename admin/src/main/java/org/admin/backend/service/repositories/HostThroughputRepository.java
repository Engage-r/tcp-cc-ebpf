package org.admin.backend.service.repositories;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.daos.HostThroughputDao;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class HostThroughputRepository {
  private final HostThroughputDao hostThroughputDao;

  public HostThroughput save(HostThroughput hostThroughput) {
    return hostThroughputDao.save(hostThroughput);
  }

  public void saveAll(List<HostThroughput> hostThroughputs) {
    hostThroughputDao.saveAll(hostThroughputs);
  }

  public List<HostThroughput> findAverageThroughputOfActiveHostsAfterTime(LocalDateTime dateTime) {
    return hostThroughputDao.findAverageThroughputOfActiveHostsAfterTime(dateTime);
  }
}
