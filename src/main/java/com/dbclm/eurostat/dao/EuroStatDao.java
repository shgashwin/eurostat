package com.dbclm.eurostat.dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;

import com.dbclm.eurostat.domain.NaceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public class EuroStatDao {

    @PersistenceContext
    private EntityManager entityManager;

    public List<NaceInfo> getNaceDetailsByCode(String code, int start,
                                               int count) {
        final Query query = entityManager.createQuery("from NaceEntity where code = :code");
        query.setParameter("code", code);
        query.setFirstResult(start);
        query.setMaxResults(count);
        return query.getResultList();
    }

    public List<NaceInfo> getNaceDetailsByOrder(String order) {
        final Query query = entityManager.createQuery("from NaceEntity where order = :order");
        query.setParameter("order", order);
        return query.getResultList();
    }
}
