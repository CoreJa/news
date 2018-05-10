package com.corechan.news.dao;

import com.corechan.news.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User,String> {
}
