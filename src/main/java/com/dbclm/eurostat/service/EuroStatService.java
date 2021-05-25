package com.dbclm.eurostat.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.dbclm.eurostat.controller.EuroStatController;
import com.dbclm.eurostat.dao.EuroStatDao;
import com.dbclm.eurostat.dao.EuroStatDaoRepository;
import com.dbclm.eurostat.domain.NaceInfo;
import com.dbclm.eurostat.entity.NaceEntity;
import com.dbclm.eurostat.exception.DuplicateOrderException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class EuroStatService {

    private static final Logger LOGGER = LoggerFactory.getLogger(EuroStatService.class);

    @Autowired
    EuroStatDao euroStatDao;

    @Autowired
    EuroStatDaoRepository euroStatDaoRepository;

    @Autowired
    ModelMapper modelMapper;

    public List<NaceInfo> getNaceDetails(String code, final String order, int start,
                                         int count) {
        if(Objects.nonNull(order)) {
            return euroStatDao.getNaceDetailsByOrder(order);
        }else if(Objects.nonNull(code)){
            return euroStatDao.getNaceDetailsByCode(code, start, count);
        }
        return new ArrayList<>();
    }

    public void postNaceDetails(NaceInfo naceInfo) {
        LOGGER.debug("naceInfo = " + naceInfo);
        final Optional<NaceEntity> naceEntity = Optional.ofNullable(naceInfo).map(this::convertTo);
        if (naceEntity.isPresent()) {
            try {
                euroStatDaoRepository.save(naceEntity.get());
            } catch (final DataIntegrityViolationException violationException) {
                LOGGER.info("naceInfo = " + naceInfo);
                throw new DuplicateOrderException("Order already exist. Try with different order.");
            }
        }
    }

    private NaceEntity convertTo(final NaceInfo naceInfo) {
        final NaceEntity naceEntity = modelMapper.map(naceInfo, NaceEntity.class);
        return naceEntity;
    }

}
