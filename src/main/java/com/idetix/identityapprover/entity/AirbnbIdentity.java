package com.idetix.identityapprover.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Airbnb_Identity_TBL")
public class AirbnbIdentity {
    @Id
    @Column(name = "PROFILEURL", unique = true, columnDefinition = "VARCHAR(250)")
    private String profileUrl;
    private String secret;
    private String ethAddress;
    private Boolean verified;
}
