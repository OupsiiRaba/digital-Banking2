package com.rochdi.digitalbankingbackend.mappers;

import com.rochdi.digitalbankingbackend.dtos.AccountOperationDTO;
import com.rochdi.digitalbankingbackend.entities.AccountOperation;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class AccountOperationMapper {

    public AccountOperationDTO fromAccountOperation (AccountOperation accountOperation){
        AccountOperationDTO accountOperationDTO = new AccountOperationDTO();
        BeanUtils.copyProperties( accountOperation, accountOperationDTO);
        return  accountOperationDTO;
    }


}
