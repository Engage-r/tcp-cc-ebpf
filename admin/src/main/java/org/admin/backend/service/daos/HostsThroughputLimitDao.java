package org.admin.backend.service.daos;

import org.admin.backend.service.models.HostsThroughputLimit;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface HostsThroughputLimitDao
    extends CrudRepository<HostsThroughputLimit, LocalDateTime> {
  @Query("SELECT htl from HostsThroughputLimit htl ORDER BY dateTime DESC LIMIT 1")
  Optional<HostsThroughputLimit> findLatestHostsThroughputLimit();
}
