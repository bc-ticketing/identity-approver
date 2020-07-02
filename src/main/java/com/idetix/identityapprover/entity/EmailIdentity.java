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
@Table(name = "Email_Identity_TBL")
public class EmailIdentity {
    @Id
    @Column(name = "EMAIL", unique = true, columnDefinition = "VARCHAR(250)")
    private String email;
    private String secret;
    private String ethAddress;
    private Boolean verified;
}