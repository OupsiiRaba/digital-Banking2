package com.rochdi.digitalbankingbackend.services;

import com.rochdi.digitalbankingbackend.dtos.AccountHistoryDTO;
import com.rochdi.digitalbankingbackend.dtos.AccountOperationDTO;
import com.rochdi.digitalbankingbackend.exceptions.BankAccountNotFoundExcetion;
import com.rochdi.digitalbankingbackend.exceptions.OperationNotFoundException;


import java.util.List;

public interface AccountOperationService {

    public List<AccountOperationDTO> getAccountOperationsHistory(String accountId);

    public AccountHistoryDTO getAccountHistory(String accountId, int page, int size) throws BankAccountNotFoundExcetion;


    AccountOperationDTO getOperation(long operationId) throws OperationNotFoundException;
}
