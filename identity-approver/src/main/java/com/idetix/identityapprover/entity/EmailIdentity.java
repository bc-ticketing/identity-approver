package com.idetix.identityapprover.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Email_Identity_TBL")
public class EmailIdentity {
    @Id
    @Column(name = "EMAIL",unique = true,columnDefinition = "VARCHAR(250)")
    private String email;
    private String ethAddress;
    private String secret;
    private Boolean verified;
}