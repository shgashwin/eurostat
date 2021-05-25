package com.dbclm.eurostat.dao;

import com.dbclm.eurostat.entity.NaceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EuroStatDaoRepository extends CrudRepository<NaceEntity, Long> {
}
