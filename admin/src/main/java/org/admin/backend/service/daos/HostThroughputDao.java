package org.admin.backend.service.daos;

import org.admin.backend.service.models.Host;
import org.admin.backend.service.models.HostThroughput;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;



public interface HostThroughputDao
    extends CrudRepository<HostThroughput, HostThroughput.ClientThroughputId> {
}
