package com.dbclm.eurostat.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Entity
@Table(name = "Nace")
@Data
public class NaceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    @NotNull
    private String order;
    private int level;
    private String code;
    @Column(length = 500)
    private String description;
    @Lob
    private String itemIncludes;
    @Column(length = 500)
    private String itemAlsoIncludes;
    private String rulings;
    @Lob
    private String itemExcludes;
    private String references;
}
