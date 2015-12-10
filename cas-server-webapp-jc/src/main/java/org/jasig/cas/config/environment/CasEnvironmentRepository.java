package org.jasig.cas.config.environment;

import java.util.Set;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CasEnvironmentRepository extends PagingAndSortingRepository<CasEnvironment, Long> {
    
    Set<CasEnvironment> findByKey(String key);
    
    @Query(value = "SELECT * FROM CAS_ENVIRONMENT p WHERE p.key = :key", nativeQuery = true)
    Set<CasEnvironment> searchNatively(@Param("key") String key);
    
    @Query(value = "FROM CasEnvironment p WHERE p.key = :key")
    Set<CasEnvironment> search(@Param("key") String key);

}
