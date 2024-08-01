package com.example.service;


import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.entity.Message;

import com.example.repository.MessageRepository;

import javafx.scene.effect.Light.Point;

@Service
public class MessageService {
    private MessageRepository messageRepository;

    @Autowired
    public MessageService(MessageRepository messageRepository){
        this.messageRepository = messageRepository;
    }

    // create a new message
    public Message createMessage(int postedBy, String message_text, long timePostedEpoch){
        Message new_message = new Message();
        new_message.setPostedBy(postedBy);
        new_message.setMessageText(message_text);
        new_message.setTimePostedEpoch(timePostedEpoch);
        return messageRepository.save(new_message);
    }

    // get all messages
    public List<Message> getAllMessages(){
        return (List<Message>) messageRepository.findAll();
    }

    // get one message given message id
    public Message getMessageById(long id){
        return messageRepository.findById(id).get();
    }

    // delete message given message id
    public void deleteMessage(long message_id){
        messageRepository.deleteById(message_id);
    }

    // update message given message id
    public Message updateMessage(long message_id, String message_text){
        Message message = getMessageById(message_id);
        if(message != null){
            message.setMessageText(message_text);
            return messageRepository.save(message);
        }
        return null;
    }

    // get all messages from user given account id
    public List<Message> getAllMessagesByAccountId(long account_id){
        return messageRepository.findAllByPostedBy(account_id);
    }
}
