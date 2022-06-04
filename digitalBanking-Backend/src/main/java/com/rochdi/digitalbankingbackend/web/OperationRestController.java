package com.rochdi.digitalbankingbackend.web;

import com.rochdi.digitalbankingbackend.dtos.AccountOperationDTO;
import com.rochdi.digitalbankingbackend.dtos.CreditDTO;
import com.rochdi.digitalbankingbackend.dtos.DebitDTO;
import com.rochdi.digitalbankingbackend.dtos.TransferRequestDTO;
import com.rochdi.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import com.rochdi.digitalbankingbackend.exceptions.BankAccountNotFoundExcetion;
import com.rochdi.digitalbankingbackend.exceptions.OperationNotFoundException;
import com.rochdi.digitalbankingbackend.services.AccountOperationService;
import com.rochdi.digitalbankingbackend.services.BankAccountService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin("*")
public class OperationRestController {


    private AccountOperationService operationService;
    private BankAccountService accountService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/operations/{id}")
    public AccountOperationDTO getOperation(@PathVariable("id") long operationId) throws OperationNotFoundException {
        return operationService.getOperation( operationId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/operations/debit")
    public DebitDTO debiter( @RequestBody DebitDTO debitDTO) throws BankAccountNotFoundExcetion, BalanceNotSufficientException {

        accountService.debit( debitDTO.getAccountId(), debitDTO.getAmount(), debitDTO.getDescription() );
        return debitDTO;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/operations/credit")
    public CreditDTO credit(@RequestBody CreditDTO creditDTO) throws BankAccountNotFoundExcetion {

        accountService.credit( creditDTO.getAccountId(), creditDTO.getAmount(), creditDTO.getDescription() );
        return creditDTO;

    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/operations/transfer")
    public TransferRequestDTO transferer(@RequestBody TransferRequestDTO transferRequestDTO) throws BankAccountNotFoundExcetion, BalanceNotSufficientException {

        accountService.transfer(
                transferRequestDTO.getAccountSourceId(),
                transferRequestDTO.getAccountDestinationId(),
                transferRequestDTO.getAmount(),
                transferRequestDTO.getDescription()
        );
        return transferRequestDTO;

    }


}
