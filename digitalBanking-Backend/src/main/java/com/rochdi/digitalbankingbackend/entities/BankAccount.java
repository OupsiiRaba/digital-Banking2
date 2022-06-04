package com.rochdi.digitalbankingbackend.entities;


import com.rochdi.digitalbankingbackend.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type", length = 7, discriminatorType = DiscriminatorType.STRING)
public class BankAccount {

    @Id
    private String id;

    private double balance;

    private Date createdAt;

    @ManyToOne
    private Customer customer;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "bankAccount", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AccountOperation> accountAccountOperations = new ArrayList<>();

}
