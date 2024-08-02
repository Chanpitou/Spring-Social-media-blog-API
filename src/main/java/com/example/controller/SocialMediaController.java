package com.example.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.Account;
import com.example.entity.Message;
import com.example.service.AccountService;
import com.example.service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your controller using Spring. The endpoints you will need can be
 * found in readme.md as well as the test cases. You be required to use the @GET/POST/PUT/DELETE/etc Mapping annotations
 * where applicable as well as the @ResponseBody and @PathVariable annotations. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a controller may be built.
 */
@RestController
public class SocialMediaController {
    private AccountService accountService;
    private MessageService messageService;

    @Autowired
    public SocialMediaController(AccountService accountService, MessageService messageService) {
        this.accountService = accountService;
        this.messageService = messageService;
    }

    // Account end-points
    // Registration
    @PostMapping("register")
    public ResponseEntity<Account> registerAccount(@RequestBody Account account){
        String validate = accountService.validate(account.getUsername(), account.getPassword());
        // if username already exist: duplicate
        if(validate.equals("duplicate")){
            return ResponseEntity.status(409).body(null);
        } 
        // if other conditions not met: others
        else if(validate.equals("others")){
            return ResponseEntity.status(400).body(null);
        } else {
            Account register_account = accountService.register(account);
            return ResponseEntity.ok().body(register_account);
        }
    }

    // login
    @PostMapping("login")
    public ResponseEntity<Account> loginAccount(@RequestBody Account account){
        Account login_account = accountService.login(account);
        if (login_account != null){
            return ResponseEntity.ok().body(login_account);
        } else {
            return ResponseEntity.status(401).body(null);
        }
    }

    // Message end-points
    // create new message
    @PostMapping("messages")
    public ResponseEntity<Message> createMessageHandler(@RequestBody Message message){
        Account account = accountService.getAccount(message.getPostedBy());
        if(account == null){
            return ResponseEntity.status(400).body(null);
        }
        Message new_message = messageService.createMessage(message);
        if(new_message != null){
            return ResponseEntity.ok().body(new_message);
        } else{
            return ResponseEntity.status(400).body(new_message);
        }
    }

    // get all messages
    @GetMapping("messages")
    public ResponseEntity<List<Message>> getAllMessagesHandler(){
        List<Message> messageList = messageService.getAllMessages();
        return ResponseEntity.ok().body(messageList);
    }

    // get message by message_id
    @GetMapping("messages/{message_id}")
    public ResponseEntity<Message> getMessageByIdHandler(@PathVariable int message_id){
        Message message = messageService.getMessageById(message_id);
        return ResponseEntity.ok().body(message);
    }

    // delete a message 
    @DeleteMapping("messages/{message_id}")
    public ResponseEntity<String> deleteMessageHandler(@PathVariable int message_id){
        Message message = messageService.getMessageById(message_id);
        if(message != null){
            messageService.deleteMessage(message_id);
            return ResponseEntity.ok().body("1");
        }
        return ResponseEntity.ok().body(null);
    }

    // update a message
    @PatchMapping("messages/{message_id}")
    public ResponseEntity<String> updateMessageHandler(@PathVariable int message_id, @RequestBody Message message){
        Message updatedMessage = messageService.updateMessage(message_id, message.getMessageText());
        if (updatedMessage != null){
            return ResponseEntity.ok().body("1");
        } 
        return ResponseEntity.status(400).body(null);
    }

    @GetMapping("accounts/{account_id}/messages")
    public ResponseEntity<List<Message>> getAllMessagesByAccountIdHandler(@PathVariable int account_id){
        List<Message> messageList = messageService.getAllMessagesByAccountId(account_id);
        return ResponseEntity.ok().body(messageList);
    }
}
