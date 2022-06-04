package com.rochdi.digitalbankingbackend.dtos;


import com.rochdi.digitalbankingbackend.enums.AccountStatus;
import lombok.Data;

import java.util.Date;


@Data
public class BankAccountRequestDTO  {

    private String id;

    private double balance;

    private Date createdAt;

    private CustomerDTO customer;


    private double overDraft;

    private String type;

    private double interestRate;


    private AccountStatus status;
}
