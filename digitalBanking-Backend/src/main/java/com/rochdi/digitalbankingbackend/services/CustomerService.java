package com.rochdi.digitalbankingbackend.services;

import com.rochdi.digitalbankingbackend.dtos.CustomerAccountsDTO;
import com.rochdi.digitalbankingbackend.dtos.CustomerDTO;
import com.rochdi.digitalbankingbackend.dtos.CustomerPageableDTO;
import com.rochdi.digitalbankingbackend.entities.Customer;
import com.rochdi.digitalbankingbackend.exceptions.CustomerNotFoundException;


import java.util.List;

public interface CustomerService {

    Customer saveCustomer(Customer customer);

    CustomerDTO saveCustomer(CustomerDTO customerDTO);

    List<CustomerDTO> listCustomers();

    CustomerDTO getCustomer(String customerId) throws CustomerNotFoundException;

    CustomerDTO updateCustomer(CustomerDTO customerDTO) throws CustomerNotFoundException;

    void deleteCustomer(String customerId);

    List<CustomerDTO> searchCustomer( String searchKeyword);


    CustomerPageableDTO searchCustomerPaginated(int page, int size, String searchKeyword);

    CustomerAccountsDTO getCustomerAccounts(String customerId, int page, int size) throws CustomerNotFoundException;

    CustomerPageableDTO paginateCustomers(int page, int size);
}
