package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    // validate the given username and password
    public String validate(String username, String password){
        // check if the username and password length meet the specifications
        if(username.length() == 0 && password.length() < 4) {
            return "others";
        }
        // check if there's already an account with the given username
        else if(accountRepository.existsByEmail(username)){
            return "duplicate";
        }
        
        return "valid";
    }

    // Registration
    public Account register(String username, String password) {

        if (validate(username, password).equals("valid")){
            // register a new account
            Account new_account = new Account();
            new_account.setUsername(username);
            new_account.setUsername(password);
            return accountRepository.save(new_account);
        }
        return null;
        
    }

    // Login
    public Account login(String username, String password){
        Account account = accountRepository.findByUsername(username);
        if(account.getUsername().equals(username) && account.getPassword().equals(password)){
            return account;
        }
        return null;
    }

}
