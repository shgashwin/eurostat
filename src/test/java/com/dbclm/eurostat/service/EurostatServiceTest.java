package com.dbclm.eurostat.service;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.dbclm.eurostat.dao.EuroStatDao;
import com.dbclm.eurostat.dao.EuroStatDaoRepository;
import com.dbclm.eurostat.domain.NaceInfo;
import com.dbclm.eurostat.entity.NaceEntity;
import com.dbclm.eurostat.exception.DuplicateOrderException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class EurostatServiceTest {

    @InjectMocks
    private EuroStatService euroStatService;

    @Mock
    private EuroStatDao euroStatDao;

    @Mock
    private EuroStatDaoRepository euroStatDaoRepository;

    @Spy
    private ModelMapper modelMapper;

    @Captor
    private ArgumentCaptor<NaceEntity> naceEntityArgumentCaptor;
    private NaceEntity naceEntity;

    @BeforeEach
    public void setUp() {
        naceEntity = new NaceEntity();
        naceEntity.setCode("A");
        naceEntity.setOrder("12345");
    }

    @Test
    public void shouldSaveNaceDetailsToDB() {

        when(euroStatDaoRepository.save(Mockito.any())).thenReturn(naceEntity);

        final NaceInfo naceInfo = new NaceInfo();
        naceInfo.setCode("A");
        naceInfo.setOrder("12345");
        euroStatService.postNaceDetails(naceInfo);
        Mockito.verify(euroStatDaoRepository).save(naceEntityArgumentCaptor.capture());
        NaceEntity value = naceEntityArgumentCaptor.getValue();
        assertThat(value.getCode(), is("A"));
        assertThat(value.getOrder(), is("12345"));
    }

    @Test
    public void shouldThrowExceptionWhenNaceDetailsAlreadyExistInDB() {

        when(euroStatDaoRepository.save(Mockito.any())).thenThrow(DataIntegrityViolationException.class);

        final NaceInfo naceInfo = new NaceInfo();
        naceInfo.setCode("A");
        naceInfo.setOrder("12345");

        Assertions.assertThrows(DuplicateOrderException.class, () ->
                euroStatService.postNaceDetails(naceInfo)
        );
    }

    @Test
    public void shouldDoNothingWhenNaceDetailsIsNull() {

        when(euroStatDaoRepository.save(Mockito.any())).thenReturn(naceEntity);

        euroStatService.postNaceDetails(null);
        Mockito.verify(euroStatDaoRepository, times(0)).save(any());
    }


    @Test
    public void shouldGetNaceDetailsByOrderWhenDataExist() {

        final List<NaceInfo> naceInfoList = new ArrayList<>();
        final NaceInfo naceInfo = new NaceInfo();
        naceInfo.setOrder("9990");
        naceInfoList.add(naceInfo);
        naceInfoList.add(naceInfo);
        when(euroStatDao.getNaceDetailsByOrder("9990")).thenReturn(naceInfoList);

        final List<NaceInfo> naceDetails = euroStatService.getNaceDetails("A", "9990",0, 20);
        assertThat(naceDetails, Matchers.hasSize(2));
    }

    @Test
    public void shouldGetNaceDetailsByCodeWhenDataExist() {

        final List<NaceInfo> naceInfoList = new ArrayList<>();
        final NaceInfo naceInfo = new NaceInfo();
        naceInfo.setOrder("9990");
        naceInfoList.add(naceInfo);
        naceInfoList.add(naceInfo);
        when(euroStatDao.getNaceDetailsByCode("A",0,20)).thenReturn(naceInfoList);

        final List<NaceInfo> naceDetails = euroStatService.getNaceDetails("A", null,0, 20);
        assertThat(naceDetails, Matchers.hasSize(2));
    }

    @Test
    public void shouldGetEmptyListOfNaceDetailsByOrderWhenDataDoesNotExist() {

        when(euroStatDao.getNaceDetailsByOrder("9990")).thenReturn(Arrays.asList());

        final List<NaceInfo> naceDetails = euroStatService.getNaceDetails("A", "9990",0, 20);
        assertThat(naceDetails, Matchers.hasSize(0));
    }

    @Test
    public void shouldGetEmptyListOfNaceDetailsByCodeWhenDataDoesNotExist() {

        when(euroStatDao.getNaceDetailsByCode("A",0,20)).thenReturn(Arrays.asList());

        final List<NaceInfo> naceDetails = euroStatService.getNaceDetails("A", null,0, 20);
        assertThat(naceDetails, Matchers.hasSize(0));
    }

}
