package com.example.geco.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.example.geco.domains.Account;


@Repository
public interface AccountRepository extends CrudRepository<Account, String>{

}