package org.admin.backend.service.daos;

import org.admin.backend.service.models.HostThroughput;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface HostThroughputDao
    extends CrudRepository<HostThroughput, HostThroughput.HostThroughputId> {

  @Query(
      "SELECT ht.host,ht.AVG(throughput) FROM HostThroughput ht WHERE (ht.dateTime>= :dateTime AND ht.host.isActive=true) GROUP BY ht.host.id")
  List<HostThroughput> findAverageThroughputOfActiveHostsAfterTime(LocalDateTime dateTime);
}
