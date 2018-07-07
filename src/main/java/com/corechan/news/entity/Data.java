package com.corechan.news.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.annotation.Generated;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "Data")
public class Data implements Comparable<Data> {
    @Id
    private Long id;
    private List<Double> list;
    private String title;
    private String content;
    private String url;
    private String img;
    private Integer topic;
    private Double maxRank;
    private Double preferenceRank;
    private String date;

    public Data() {
    }

    public Data(Long id, List<Double> list, String title, String content, String url, String img, Integer topic, Double maxRank, Double preferenceRank, String date) {
        this.id = id;
        this.list = list;
        this.title = title;
        this.content = content;
        this.url = url;
        this.img = img;
        this.topic = topic;
        this.maxRank = maxRank;
        this.preferenceRank = preferenceRank;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Double> getList() {
        return list;
    }

    public void setList(List<Double> list) {
        this.list = list;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public Integer getTopic() {
        return topic;
    }

    public void setTopic(Integer topic) {
        this.topic = topic;
    }

    public Double getMaxRank() {
        return maxRank;
    }

    public void setMaxRank(Double maxRank) {
        this.maxRank = maxRank;
    }

    public Double getPreferenceRank() {
        return preferenceRank;
    }

    public void setPreferenceRank(Double preferenceRank) {
        this.preferenceRank = preferenceRank;
    }

    @Override
    public int compareTo(Data o) {
        return o.getPreferenceRank().compareTo(this.getPreferenceRank());
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
