package com.rochdi.digitalbankingbackend.services;

import com.rochdi.digitalbankingbackend.dtos.BankAccountDTO;
import com.rochdi.digitalbankingbackend.dtos.CurrentBankAccountDTO;
import com.rochdi.digitalbankingbackend.dtos.SavingBankAccountDTO;
import com.rochdi.digitalbankingbackend.entities.*;
import com.rochdi.digitalbankingbackend.enums.AccountStatus;
import com.rochdi.digitalbankingbackend.enums.OperationType;
import com.rochdi.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import com.rochdi.digitalbankingbackend.exceptions.BankAccountNotFoundExcetion;
import com.rochdi.digitalbankingbackend.exceptions.CustomerNotFoundException;
import com.rochdi.digitalbankingbackend.mappers.BankAccountMapper;
import com.rochdi.digitalbankingbackend.mappers.CustomerMapper;
import com.rochdi.digitalbankingbackend.repositories.AccountOperationRepository;
import com.rochdi.digitalbankingbackend.repositories.BankAccountRepository;
import com.rochdi.digitalbankingbackend.repositories.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@Slf4j // equals adding this line :     private Logger log = LoggerFactory.getLogger(this.getClass());
public class BankAccountServiceImpl implements BankAccountService {

    private CustomerRepository customerRepository;
    private BankAccountRepository accountRepository;
    private AccountOperationRepository operationRepository;
    private BankAccountMapper bankAccountMapper;
    private CustomerMapper customerMapper;

    @Override
    public CurrentBankAccountDTO saveCurrentBankAccount(double initialBalance, double overDraft, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException(" Customer not found !! ");

        CurrentAccount currentBankAccount;
        currentBankAccount = new CurrentAccount();
        currentBankAccount.setId(UUID.randomUUID().toString());
        currentBankAccount.setCreatedAt(new Date());
        currentBankAccount.setBalance(initialBalance);
        currentBankAccount.setCustomer(customer);
        currentBankAccount.setOverDraft(overDraft);
        currentBankAccount.setStatus(AccountStatus.CREATED);

        CurrentAccount savedBankAccount = accountRepository.save(currentBankAccount);
        return bankAccountMapper.fromCurrentAccount(savedBankAccount);
    }

    @Override
    public BankAccountDTO updateCurrentBankAccount(CurrentAccount currentAccount) throws BankAccountNotFoundExcetion {
        CurrentAccount currentAccount1 = new CurrentAccount();
        CurrentBankAccountDTO currentBankAccountDTO = (CurrentBankAccountDTO) this.getBankAccount(currentAccount.getId());
        BeanUtils.copyProperties( currentBankAccountDTO, currentAccount1);
        currentAccount1.setCustomer( customerMapper.fromCustomerDto(currentBankAccountDTO.getCustomer()) );

        currentAccount1.setOverDraft(currentAccount.getOverDraft());
        currentAccount1.setStatus(currentAccount.getStatus());

        CurrentAccount savedBankAccount = accountRepository.save(currentAccount1);

        return getBankAccount(currentAccount.getId());
    }

    @Override
    public SavingBankAccountDTO saveSavingBankAccount(double initialBalance, double interestRate, String customerId) throws CustomerNotFoundException {
        Customer customer = customerRepository.findById(customerId).orElse(null);
        if (customer == null)
            throw new CustomerNotFoundException(" Customer not found !! ");

        SavingAccount savingBankAccount;
        savingBankAccount = new SavingAccount();
        savingBankAccount.setId(UUID.randomUUID().toString());
        savingBankAccount.setCreatedAt(new Date());
        savingBankAccount.setBalance(initialBalance);
        savingBankAccount.setCustomer(customer);
        savingBankAccount.setInterestRate(interestRate);
        savingBankAccount.setStatus(AccountStatus.CREATED);

        SavingAccount savedBankAccount = accountRepository.save(savingBankAccount);
        return bankAccountMapper.fromSavingAccount(savingBankAccount);
    }


    @Override
    public BankAccountDTO updateSavingBankAccount(SavingAccount savingAccount) throws BankAccountNotFoundExcetion {
        SavingAccount savingAccount1 = new SavingAccount();
        SavingBankAccountDTO savingBankAccountDTO = (SavingBankAccountDTO) this.getBankAccount(savingAccount.getId());
        BeanUtils.copyProperties( savingBankAccountDTO, savingAccount1);
        savingAccount1.setCustomer( customerMapper.fromCustomerDto(savingBankAccountDTO.getCustomer()) );

        savingAccount1.setInterestRate(savingAccount.getInterestRate());
        savingAccount1.setStatus(savingAccount.getStatus());

        SavingAccount savedBankAccount = accountRepository.save(savingAccount1);

        return getBankAccount(savingAccount.getId());
    }


    @Override
    public BankAccountDTO getBankAccount(String bankAccountId) throws BankAccountNotFoundExcetion {
        BankAccount bankAccount = accountRepository.findById(bankAccountId).orElseThrow(() -> new BankAccountNotFoundExcetion("Bank account not found !"));
        if (bankAccount instanceof SavingAccount)
            return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
        return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
    }

    @Override
    public void debit(String accountId, double amount, String description) throws BankAccountNotFoundExcetion, BalanceNotSufficientException {
        BankAccount bankAccount = accountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundExcetion("Bank account not found !"));
        if (bankAccount.getBalance() < amount)
            throw new BalanceNotSufficientException("Balance not sufficient for transaction !");
        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.DEBIT);
        operation.setAmount(amount);
        operation.setOperationDate(new Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() - amount);
        accountRepository.save(bankAccount);
    }

    @Override
    public void credit(String accountId, double amount, String description) throws BankAccountNotFoundExcetion {
        BankAccount bankAccount = accountRepository.findById(accountId).orElseThrow(() -> new BankAccountNotFoundExcetion("Bank account not found !"));

        AccountOperation operation = new AccountOperation();
        operation.setType(OperationType.CREDIT);
        operation.setAmount(amount);
        operation.setOperationDate(new Date());
        operation.setDescription(description);
        operation.setBankAccount(bankAccount);
        operationRepository.save(operation);
        bankAccount.setBalance(bankAccount.getBalance() + amount);
        accountRepository.save(bankAccount);
    }

    @Override
    public void transfer(String accountSourceId, String accountDestinationId, double amount, String description) throws BankAccountNotFoundExcetion, BalanceNotSufficientException {
        debit(accountSourceId, amount, description);
        credit(accountDestinationId, amount, description);
    }

    @Override
    public List<BankAccount> listBankAccount() {
        return accountRepository.findAll();
    }

    @Override
    public List<BankAccountDTO> listBankAccountDto() {
        List<BankAccount> bankAccounts = accountRepository.findAll();
        List<BankAccountDTO> bankAccountDTOS = bankAccounts.stream().map(bankAccount -> {
            if (bankAccount instanceof SavingAccount)
                return bankAccountMapper.fromSavingAccount((SavingAccount) bankAccount);
            return bankAccountMapper.fromCurrentAccount((CurrentAccount) bankAccount);
        }).collect(Collectors.toList());
        return bankAccountDTOS;
    }


    @Override
    public void deleteAccount(String accountId) {
        accountRepository.deleteById(accountId);
    }

}
