package com.corechan.news.entity;

import org.springframework.data.annotation.Id;

import java.util.HashMap;
import java.util.Map;

public class Statistic {
    @Id
    private Long id;
    private String maxCountTopic;
    private Map<String, Integer> topic=new HashMap<>();

    public Statistic() {
    }

    public String getMaxCountTopic() {
        return maxCountTopic;
    }

    public void setMaxCountTopic(String maxCountTopic) {
        this.maxCountTopic = maxCountTopic;
    }

    public Map<String, Integer> getTopic() {
        return topic;
    }

    public void setTopic(Map<String, Integer> topic) {
        this.topic = topic;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
