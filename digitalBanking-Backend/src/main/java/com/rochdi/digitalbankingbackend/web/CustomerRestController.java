package com.rochdi.digitalbankingbackend.web;

import com.rochdi.digitalbankingbackend.dtos.CustomerAccountsDTO;
import com.rochdi.digitalbankingbackend.dtos.CustomerDTO;
import com.rochdi.digitalbankingbackend.dtos.CustomerPageableDTO;
import com.rochdi.digitalbankingbackend.exceptions.CustomerNotFoundException;
import com.rochdi.digitalbankingbackend.services.CustomerService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CustomerRestController {

    private CustomerService customerService;

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customers")
    public List<CustomerDTO> getCustomers(){
        return customerService.listCustomers();
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customers/paginated")
    public CustomerPageableDTO getPaginatedCustomers(
            @RequestParam(name = "page", defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ){
        return customerService.paginateCustomers( page, size);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customers/paginated/search")
    public CustomerPageableDTO searchCustomersPaginated( @RequestParam(name = "keyword", defaultValue = "") String keyword,
                                                         @RequestParam(name = "page", defaultValue = "0") int page,
                                                         @RequestParam(name = "size", defaultValue = "10") int size){
        return customerService.searchCustomerPaginated( page, size, keyword);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customers/search")
    public List<CustomerDTO> getCustomers( @RequestParam(name = "keyword", defaultValue = "") String keyword ){
        return customerService.searchCustomer( keyword);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customers/{id}")
    public CustomerDTO getCustomer( @PathVariable(name="id") String customerId) throws CustomerNotFoundException {
         return customerService.getCustomer( customerId);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PostMapping("/customers")
    public CustomerDTO saveCustomer( @RequestBody CustomerDTO request){
        return customerService.saveCustomer( request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/customers/{id}")
    public CustomerDTO updateCustomer( @PathVariable(name="id") String customerId, @RequestBody CustomerDTO request) throws CustomerNotFoundException {
        request.setId( customerId);
        return customerService.updateCustomer( request);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/customers/{id}")
    public void deleteCustomer(@PathVariable(name="id") String customerId){
        customerService.deleteCustomer( customerId);
    }

    @PreAuthorize("hasAuthority('USER')")
    @GetMapping("/customers/{id}/accounts")
    public CustomerAccountsDTO getCustomer(@PathVariable(name="id") String customerId,
                                           @RequestParam(name = "page", defaultValue = "0") int page,
                                           @RequestParam(name = "size", defaultValue = "10") int size)
            throws CustomerNotFoundException {
        return customerService.getCustomerAccounts( customerId, page, size);
    }

}
