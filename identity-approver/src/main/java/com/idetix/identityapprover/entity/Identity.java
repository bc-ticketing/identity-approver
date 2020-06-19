package com.idetix.identityapprover.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Approved_Identity_TBL")
public class Identity {
    @Id
    @Column(name = "ETH_ADDRESS",unique = true,columnDefinition = "VARCHAR(42)")
    private String ethAddress;
    private String email;
    private String handyNr;
    private String airBnB;

}
