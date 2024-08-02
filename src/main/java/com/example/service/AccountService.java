package com.example.service;

import java.util.Optional;

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

    // Registration
    public Account register(Account account) {

        final boolean ACCOUNT_VALIDATED = validate(account.getUsername(), account.getPassword()).equals("valid");
        
        if (ACCOUNT_VALIDATED){
            Account new_account = new Account();
            new_account.setUsername(account.getUsername());
            new_account.setPassword(account.getPassword());
            return accountRepository.save(new_account);
        }
        return null;
    }

    // Login
    public Account login(Account account){
        Optional<Account> login_account = accountRepository.findByUsername(account.getUsername());
        if (!login_account.isPresent()){
            return null;
        } 
        
        Account get_account = login_account.get();
        // login requirements
        final boolean USERNAME_EQUALS = account.getUsername().equals(get_account.getUsername());
        final boolean PASSWORD_EQUALS = account.getPassword().equals(get_account.getPassword());

        if( USERNAME_EQUALS && PASSWORD_EQUALS ){
            return get_account;
        } else{
            return null;
        }
    }

    // (helper function) validate the given username and password
    public String validate(String username, String password){
        Optional<Account> account = accountRepository.findByUsername(username);

        // conditions for registration
        final boolean USERNAME_BLANK = username.length() == 0;
        final boolean PASSWORD_TOO_SHORT = password.length() < 4;
        final boolean USERNAME_EXIST = account.isPresent();

        if(USERNAME_BLANK || PASSWORD_TOO_SHORT) {
            return "others";
        } else if(USERNAME_EXIST){
            return "duplicate";
        } else {
            return "valid";
        }
    }

    // (Helper function) get account
    public Account getAccount(int account_id){
        Optional<Account> account = accountRepository.findById(account_id);
        if(account.isPresent()){
            return account.get();
        }
        return null;
    }

}
