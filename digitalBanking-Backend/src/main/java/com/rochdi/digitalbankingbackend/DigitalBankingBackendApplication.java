package com.rochdi.digitalbankingbackend;

import com.github.javafaker.Faker;
import com.rochdi.digitalbankingbackend.entities.*;
import com.rochdi.digitalbankingbackend.enums.AccountStatus;
import com.rochdi.digitalbankingbackend.enums.OperationType;
import com.rochdi.digitalbankingbackend.exceptions.BalanceNotSufficientException;
import com.rochdi.digitalbankingbackend.exceptions.BankAccountNotFoundExcetion;
import com.rochdi.digitalbankingbackend.exceptions.CustomerNotFoundException;
import com.rochdi.digitalbankingbackend.repositories.AccountOperationRepository;
import com.rochdi.digitalbankingbackend.repositories.BankAccountRepository;
import com.rochdi.digitalbankingbackend.repositories.CustomerRepository;
import com.rochdi.digitalbankingbackend.security.entities.AppRole;
import com.rochdi.digitalbankingbackend.security.entities.AppUser;
import com.rochdi.digitalbankingbackend.security.service.SecurityService;
import com.rochdi.digitalbankingbackend.services.BankAccountService;
import com.rochdi.digitalbankingbackend.services.BankService;
import com.rochdi.digitalbankingbackend.services.CustomerService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@SpringBootApplication
public class DigitalBankingBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DigitalBankingBackendApplication.class, args);
	}

	@Bean
	PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}

	@Bean
	CommandLineRunner instanciate_user(SecurityService securityService){
		return args -> {
			securityService.addNewRole(new AppRole(null,"USER"));
			securityService.addNewRole(new AppRole(null,"ADMIN"));
			securityService.addNewUser( new AppUser( null, "user", "12345", new ArrayList<>()));
			securityService.addNewUser( new AppUser( null, "rabab", "12345", new ArrayList<>()));

			securityService.addRoleToUser( "user", "USER");
			securityService.addRoleToUser( "rabab", "USER");
			securityService.addRoleToUser( "rabab", "ADMIN");

		};
	}

	@Bean
	CommandLineRunner fillDbUsingBAService(
			BankAccountService bankAccountService,
			CustomerService customerService
	){
		return  args -> {
			Faker faker = new Faker();

			for( int i=0; i<13; i++){
				Customer customer = new Customer();
				customer.setId(UUID.randomUUID().toString());
				customer.setName( faker.name().fullName() );
				customer.setEmail( faker.internet().safeEmailAddress() );
				customerService.saveCustomer( customer );
			}


			customerService.listCustomers().forEach(customer -> {

				try {
					bankAccountService.saveCurrentBankAccount(
							faker.number().randomDouble(2, 10000, 9999999),
							faker.number().randomDouble(2, 500, 9000),
							customer.getId()
					);

					bankAccountService.saveSavingBankAccount(
							faker.number().randomDouble(2, 10000, 9999999),
							faker.number().randomDouble(2, 0, 100),
							customer.getId()
					);


				} catch (CustomerNotFoundException e) {
					e.printStackTrace();
				}


			});

			try {
				for (BankAccount account : bankAccountService.listBankAccount()) {
					for (int i = 0; i < faker.random().nextInt(8, 11); i++) {
						bankAccountService.credit(account.getId(), 17 * faker.number().randomDouble(2, 111, 1111), "Cr??dit");
						bankAccountService.debit(account.getId(), 17 * faker.number().randomDouble(2, 111, 1111), "Debit");
					}
				};
			} catch (BankAccountNotFoundExcetion | BalanceNotSufficientException e) {
				e.printStackTrace();
			}


		};
	}


	@Bean
	CommandLineRunner testingFunction(BankService bankService){
		return args -> {
			bankService.consulter();
		};
	}


	@Bean
	CommandLineRunner users(
			CustomerRepository customerRepository,
			BankAccountRepository bankAccountRepository,
			AccountOperationRepository accountOperationRepository
	){
		return args -> {

			Faker faker = new Faker();

			for( int i=0; i<13; i++){
				Customer customer = new Customer();
				customer.setId(UUID.randomUUID().toString());
				customer.setName( faker.name().fullName() );
				customer.setEmail( faker.internet().safeEmailAddress() );
				customerRepository.save( customer );
			}

			customerRepository.findAll().forEach(customer -> {
				CurrentAccount currentAccount = new CurrentAccount();
				currentAccount.setId( UUID.randomUUID().toString() );
				currentAccount.setCustomer( customer);
				currentAccount.setBalance( faker.number().randomDouble(2, 10, 9999999) );
				currentAccount.setCreatedAt( faker.date().past( 600, TimeUnit.DAYS) );
				currentAccount.setStatus( AccountStatus.CREATED);
				currentAccount.setOverDraft( faker.number().randomDouble(2, 500, 9000) );
				bankAccountRepository.save( currentAccount );

				SavingAccount savingAccount = new SavingAccount();
				savingAccount.setId( UUID.randomUUID().toString() );
				savingAccount.setCustomer( customer);
				savingAccount.setBalance( faker.number().randomDouble(2, 10, 9999999) );
				savingAccount.setCreatedAt( faker.date().past( 800, TimeUnit.DAYS) );
				savingAccount.setStatus( AccountStatus.CREATED);
				savingAccount.setInterestRate( faker.number().randomDouble(2, 0, 100) );
				bankAccountRepository.save( savingAccount );
			});


			bankAccountRepository.findAll().forEach( bankAccount -> {

				for( int i=0; i<faker.random().nextInt(3,6); i++){
					AccountOperation accountOperation = new AccountOperation();
					accountOperation.setAmount( faker.number().randomDouble(2, 100, 15000));
					accountOperation.setOperationDate( faker.date().past( 200, TimeUnit.DAYS) );
					accountOperation.setBankAccount( bankAccount);
					accountOperation.setType( faker.options().option(OperationType.class) );
					accountOperationRepository.save( accountOperation );
				}

			});

		};
	}

}
