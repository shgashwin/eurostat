package com.dbclm.eurostat.it;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.*;

import java.util.Random;

import com.dbclm.eurostat.domain.NaceInfo;
import com.dbclm.eurostat.exception.DuplicateOrderException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@PropertySource("application-test.properties")
@ActiveProfiles("test")
public class EuroStatNaceIT {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @LocalServerPort
    private int randomServerPort;

    @Test
    public void shouldSaveNaceDetails() {

        final NaceInfo naceInfo = new NaceInfo();
        naceInfo.setCode("A");
        String order = Integer.toString(new Random().nextInt(10000) + 1);
        naceInfo.setOrder(order);
        final HttpEntity<NaceInfo> requestBody = new HttpEntity<>(naceInfo);
        final String url = "http://localhost:" + randomServerPort + "/rest/eurostats";
        final ResponseEntity responseEntity = testRestTemplate.postForEntity(url, requestBody, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CREATED));
        assertThat(responseEntity.getHeaders().get("Location"), is(contains(url + "/"+order)));
    }

    @Test
    public void shouldThrowExceptionWhenTryToSaveNaceDetailsWithSameOrderId() {

        final NaceInfo naceInfo = new NaceInfo();
        naceInfo.setCode("A");
        naceInfo.setOrder(Integer.toString(new Random().nextInt(10000) + 1));
        final HttpEntity<NaceInfo> requestBody = new HttpEntity<>(naceInfo);
        final String url = "http://localhost:" + randomServerPort + "/rest/eurostats";
        testRestTemplate.postForEntity(url, requestBody, String.class);
        final ResponseEntity responseEntity = testRestTemplate.postForEntity(url, requestBody, String.class);
        assertThat(responseEntity.getStatusCode(), is(HttpStatus.CONFLICT));
    }

}
