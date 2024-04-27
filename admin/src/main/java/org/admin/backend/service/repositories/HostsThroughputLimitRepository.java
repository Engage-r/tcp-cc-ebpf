package org.admin.backend.service.repositories;

import lombok.RequiredArgsConstructor;
import org.admin.backend.service.daos.HostsThroughputLimitDao;
import org.admin.backend.service.exceptions.HostsThroughputLimitNotFoundException;
import org.admin.backend.service.models.HostsThroughputLimit;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class HostsThroughputLimitRepository {
  private final HostsThroughputLimitDao hostsThroughputLimitDao;

  public HostsThroughputLimit findLatestHostsThroughputLimit() {
    return hostsThroughputLimitDao
        .findLatestHostsThroughputLimit()
        .orElseThrow(HostsThroughputLimitNotFoundException::new);
  }

  public HostsThroughputLimit save(HostsThroughputLimit hostsThroughputLimit) {
    return hostsThroughputLimitDao.save(hostsThroughputLimit);
  }
}
