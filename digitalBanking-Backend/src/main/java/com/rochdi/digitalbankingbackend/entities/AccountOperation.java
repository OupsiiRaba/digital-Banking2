package com.rochdi.digitalbankingbackend.entities;

import com.rochdi.digitalbankingbackend.enums.OperationType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;



@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AccountOperation {

    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private long id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date operationDate;

    private double amount;

    @Enumerated(EnumType.STRING)
    private OperationType type;

    @ManyToOne
    private BankAccount bankAccount;

    private String description;
}
