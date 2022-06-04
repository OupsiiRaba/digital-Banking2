package com.rochdi.digitalbankingbackend.mappers;

import com.rochdi.digitalbankingbackend.dtos.CustomerDTO;
import com.rochdi.digitalbankingbackend.entities.Customer;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class CustomerMapper {

    public CustomerDTO fromCustomer(Customer customer){
        CustomerDTO customerDTO = new CustomerDTO();
        BeanUtils.copyProperties( customer, customerDTO);
        return customerDTO;
    }

    public Customer fromCustomerDto( CustomerDTO customerDto){
        Customer customer = new Customer();
        BeanUtils.copyProperties( customerDto, customer);
        return customer;
    }

}
