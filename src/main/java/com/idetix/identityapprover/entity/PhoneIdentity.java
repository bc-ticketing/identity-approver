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
@Table(name = "Phone_Identity_TBL")
public class PhoneIdentity {
    @Id
    @Column(name = "PHONENR", unique = true, columnDefinition = "VARCHAR(42)")
    private String phoneNr;
    private String secret;
    private String ethAddress;
    private Boolean verified;
}