package com.dbclm.eurostat.domain;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class NaceInfo {
    @NotNull
    private String order;
    private int level;
    private String code;
    private String parent;
    private String description;
    private String itemIncludes;
    private String itemAlsoIncludes;
    private String rulings;
    private String itemExcludes;
    private String references;
}
