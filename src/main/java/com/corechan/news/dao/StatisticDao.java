package com.corechan.news.dao;

import com.corechan.news.entity.Statistic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StatisticDao extends MongoRepository<Statistic, Integer> {
}
