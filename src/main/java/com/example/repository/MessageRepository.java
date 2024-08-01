package com.example.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.entity.Message;

public interface MessageRepository extends JpaRepository<Message, Long>{
    List<Message> findAllByPostedBy(Long postedBy);
}
