package com.dbclm.eurostat.controller;

import java.net.URI;
import java.util.List;
import java.util.Objects;

import com.dbclm.eurostat.domain.NaceInfo;
import com.dbclm.eurostat.exception.InvalidParametersException;
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
    public List<NaceInfo> getNaceDetails(@RequestParam(required = false) final String code, @RequestParam(required = false) final String order,
                                         @RequestParam(defaultValue = "0") final int start,
                                         @RequestParam(defaultValue = "20") int count) throws Exception {
        LOGGER.debug("code = " + code + ", start = " + start + ", count = " + count);
        count = count > 20 ? 20 : count;
        if (Objects.nonNull(code) || Objects.nonNull(order)) {
            return euroStatService.getNaceDetails(code, order, start, count);
        }
        throw new InvalidParametersException("Invalid Parameters");

    }

    @PostMapping
    public ResponseEntity<Object> postNaceDetails(@RequestBody NaceInfo naceInfo) {
        LOGGER.debug("naceInfo = " + naceInfo);
        euroStatService.postNaceDetails(naceInfo);
        final URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(naceInfo.getOrder())
                .toUri();

        return ResponseEntity.created(location).build();
    }
}

