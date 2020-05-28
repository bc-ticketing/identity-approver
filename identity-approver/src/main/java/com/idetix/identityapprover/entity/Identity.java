package com.idetix.identityapprover.entity;

        import lombok.AllArgsConstructor;
        import lombok.Data;
        import lombok.NoArgsConstructor;

        import javax.persistence.Entity;
        import javax.persistence.GeneratedValue;
        import javax.persistence.Id;
        import javax.persistence.Table;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Approved_Identity_TBL")
public class Identity {
    @Id
    @GeneratedValue
    private int id;
    private String ethAdress;
    private String email;
    private String handyNr;
    private String airBnB;

}
