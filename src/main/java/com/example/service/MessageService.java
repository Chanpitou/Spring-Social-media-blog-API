package com.example.service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.entity.Message;
import com.example.repository.MessageRepository;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    // create a new message
    public Message createMessage(Message message){
        // conditions to create a new message
        final boolean MESSAGE_TEXT_BLANK = message.getMessageText().length() == 0;
        final boolean MESSAGE_TEXT_TOO_LONG = message.getMessageText().length() >= 225;

        if( MESSAGE_TEXT_BLANK || MESSAGE_TEXT_TOO_LONG ){
            return null;
        } else{
            return messageRepository.save(message);
        }
    }

    // get all messages
    public List<Message> getAllMessages(){
        return (List<Message>) messageRepository.findAll();
    }

    // get one message given message id
    public Message getMessageById(int message_id){
        Optional<Message> message = messageRepository.findById(message_id);
        if(message.isPresent()){
            return message.get();
        }
        return null;
    }

    // delete message given message id
    public void deleteMessage(int message_id){
        messageRepository.deleteById(message_id);
    }

    // update message given message id
    public Message updateMessage(int message_id, String message_text){
        Message updated_message = getMessageById(message_id);
        
        // update conditions
        final boolean MESSAGE_BLANK = message_text.length() == 0;
        final boolean MESSAGE_LENGTH_EXCEEDED = message_text.length() >= 255;
        final boolean MESSAGE_EXIST = updated_message != null;
        
        if (MESSAGE_BLANK || MESSAGE_LENGTH_EXCEEDED) {
            return null;
        } else if (MESSAGE_EXIST) {
            updated_message.setMessageText(message_text);
            return messageRepository.save(updated_message);
        } else {
            return null;
        }
    }

    // get all messages from user given account id
    public List<Message> getAllMessagesByAccountId(int account_id){
        return (List<Message>) messageRepository.findAllByPostedBy(account_id);
    }
}
