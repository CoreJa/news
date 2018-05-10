package com.corechan.news.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "User")
public class User {
    @Id
    private String userId;
    private String password;
    private String name;
    private String img;
    private List<Double> preference = new ArrayList<>();
    private List<Log> history = new ArrayList<>();

    public User() {
    }

    public User(String userId, String password, String name) {
        this.userId = userId;
        this.password = password;
        this.name = name;
        this.img = "/img/user_default.png";
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public List<Double> getPreference() {
        return preference;
    }

    public void setPreference(List<Double> preference) {
        this.preference = preference;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public List<Log> getHistory() {
        return history;
    }

    public void setHistory(List<Log> history) {
        this.history = history;
    }
}
