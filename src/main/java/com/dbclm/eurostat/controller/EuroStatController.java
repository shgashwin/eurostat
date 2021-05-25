package com.dbclm.eurostat.controller;

import java.net.URI;
import java.util.List;

import com.dbclm.eurostat.domain.NaceInfo;
import com.dbclm.eurostat.service.EuroStatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/rest/eurostats")
public class EuroStatController {

    private static final Logger LOGGER = LoggerFactory.getLogger(EuroStatController.class);

    @Autowired
    EuroStatService euroStatService;

    @GetMapping
    public List<NaceInfo> getNaceDetails(@RequestParam(required = false) String code, @RequestParam(required = false) String order,
                                         @RequestParam(defaultValue = "0") int start,
                                         @RequestParam(defaultValue = "20") int count) {
        LOGGER.debug("code = " + code + ", start = " + start + ", count = " + count);
        count = count > 20 ? 20 : count;
        return euroStatService.getNaceDetails(code, order, start, count);
    }

    @PostMapping
    public ResponseEntity<Object> postNaceDetails(@RequestBody NaceInfo naceInfo) {
        LOGGER.debug("naceInfo = " + naceInfo);
        euroStatService.postNaceDetails(naceInfo);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(naceInfo.getOrder())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}

