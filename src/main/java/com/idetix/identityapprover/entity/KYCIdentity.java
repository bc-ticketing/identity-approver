package com.idetix.identityapprover.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "KYC_Identity_TBL")
public class KYCIdentity {
    @Id
    @Column(name = "MRZ", unique = true, columnDefinition = "VARCHAR(250)")
    protected String mrz;
    protected String idNumber;
    protected MRZType type;
    protected Boolean isValid;
    protected Date date;
    private String ethAddress;
    private Boolean verified;
}
