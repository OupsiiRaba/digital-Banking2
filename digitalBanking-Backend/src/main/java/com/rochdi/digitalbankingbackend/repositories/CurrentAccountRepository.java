package com.rochdi.digitalbankingbackend.repositories;

import com.rochdi.digitalbankingbackend.entities.CurrentAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CurrentAccountRepository extends JpaRepository<CurrentAccount, String> {
}
