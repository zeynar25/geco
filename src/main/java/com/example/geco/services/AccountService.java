package com.example.geco.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.geco.domains.Account;
import com.example.geco.repositories.AccountRepository;

@Service
public class AccountService {
	@Autowired
	private AccountRepository accountRepository;
	
	public Account addAccount(Account account) {
		return accountRepository.save(account);
	}
}