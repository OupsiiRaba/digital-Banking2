package com.rochdi.digitalbankingbackend.services;


import com.rochdi.digitalbankingbackend.dtos.BankAccountDTO;
import com.rochdi.digitalbankingbackend.dtos.CurrentBankAccountDTO;
import com.rochdi.digitalbankingbackend.dtos.SavingBankAccountDTO;
import com.rochdi.digitalbankingbackend.entities.BankAccount;
import com.rochdi.digitalbankingbackend.entities.CurrentAccount;
import com.rochdi.digitalbankingbackend.entities.SavingAccount;
import com.rochdi.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import com.rochdi.digitalbankingbackend.exceptions.BankAccountNotFoundExcetion;
import com.rochdi.digitalbankingbackend.exceptions.CustomerNotFoundException;


import java.util.List;


public interface BankAccountService {


    CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException;

    BankAccountDTO updateCurrentBankAccount(CurrentAccount currentAccount) throws BankAccountNotFoundExcetion;

    SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException;


    BankAccountDTO updateSavingBankAccount(SavingAccount savingAccount) throws BankAccountNotFoundExcetion;

    BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundExcetion;

    void debit( String accountId, double amount, String description) throws BankAccountNotFoundExcetion, BalanceNotSufficientException;

    void credit( String accountId, double amount, String description) throws BankAccountNotFoundExcetion;

    void transfer( String accountSourceId, String accountDestinationId, double amount, String description) throws BankAccountNotFoundExcetion, BalanceNotSufficientException;

    List<BankAccount> listBankAccount();

    List<BankAccountDTO> listBankAccountDto();

    void deleteAccount(String accountId);
}
