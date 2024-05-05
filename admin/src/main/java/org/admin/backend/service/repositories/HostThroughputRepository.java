package org.admin.backend.service.repositories;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.daos.HostThroughputDao;
import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
    LocalDateTime dateTimeTOConsider = dateTime;
    if (dateTime.isBefore(LocalDateTime.now().minusSeconds(5)))
      dateTimeTOConsider = LocalDateTime.now().minusSeconds(5);
    List<Object[]> hostThroughputDTOS =
        hostThroughputDao.findAverageThroughputOfActiveHostsAfterTime(dateTimeTOConsider);
    List<HostThroughput> hostsThroughput = new ArrayList<>();
    for (Object[] hostThroughputDTO : hostThroughputDTOS) {
      HostThroughput hostThroughput = new HostThroughput();
      hostThroughput.setHost((Host) hostThroughputDTO[0]);
      hostThroughput.setThroughput((Double) hostThroughputDTO[1]);
      hostsThroughput.add(hostThroughput);
    }
    return hostsThroughput;
  }
}
