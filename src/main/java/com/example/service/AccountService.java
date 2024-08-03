package com.example.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Account;
import com.example.exception.BadRequestException;
import com.example.exception.DuplicateUsernameException;
import com.example.exception.UnauthorizedException;
import com.example.repository.AccountRepository;

@Service
public class AccountService {
    private AccountRepository accountRepository;
    
    @Autowired
    public AccountService(AccountRepository accountRepository){
        this.accountRepository = accountRepository;
    }

    // Registration
    public Account register(Account account) throws BadRequestException, DuplicateUsernameException {
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
    public Account login(Account account) throws UnauthorizedException{
        Account login_Account = accountRepository.findByUsername(account.getUsername())
            .orElseThrow(() -> new UnauthorizedException("Account with username " + account.getUsername() + " does not exist."));
        
        // login requirements
        final boolean USERNAME_EQUALS = account.getUsername().equals(login_Account.getUsername());
        final boolean PASSWORD_EQUALS = account.getPassword().equals(login_Account.getPassword());

        if( USERNAME_EQUALS && PASSWORD_EQUALS ){
            return login_Account;
        } 
        throw new UnauthorizedException("Given Username or Password does not match.");

    }

    // (helper function) validate the given username and password
    public String validate(String username, String password) throws BadRequestException, DuplicateUsernameException{
        Optional<Account> account = accountRepository.findByUsername(username);

        // conditions for registration
        final boolean USERNAME_BLANK = username.length() == 0;
        final boolean PASSWORD_TOO_SHORT = password.length() < 4;
        final boolean USERNAME_EXIST = account.isPresent();

        if(USERNAME_BLANK || PASSWORD_TOO_SHORT) {
            throw new BadRequestException("The given Username or Password does not meet the requirement.");
        } else if(USERNAME_EXIST){
            throw new DuplicateUsernameException("The given Username " + username + " alreadys exist.");
        } else {
            return "valid";
        }
    }

    // (Helper function) check if an account exist
    public void checkAccount(int account_id) throws BadRequestException{
        accountRepository.findById(account_id)
                .orElseThrow(() -> new BadRequestException("Account with id " + account_id + " does not exist."));
    }
}
