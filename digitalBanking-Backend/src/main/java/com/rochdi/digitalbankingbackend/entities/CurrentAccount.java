package com.rochdi.digitalbankingbackend.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CURRENT")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CurrentAccount extends BankAccount{

    private double overDraft;

}
