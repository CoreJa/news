package com.corechan.news.dao;

import com.corechan.news.entity.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;


public interface DataDao extends MongoRepository<Data, Integer> {
    Page<Data> findByTopicOrderByMaxRankDesc(Integer topic,Pageable pageable);
}
