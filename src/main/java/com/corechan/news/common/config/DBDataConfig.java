package com.corechan.news.common.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "data")
public class DBDataConfig {
    private String net_news_data;
    private String theta;
    private String img;
    private String dbHome;

    public DBDataConfig() {
    }

    public String getNet_news_data() {
        return net_news_data;
    }

    public void setNet_news_data(String net_news_data) {
        this.net_news_data = net_news_data;
    }

    public String getTheta() {
        return theta;
    }

    public void setTheta(String theta) {
        this.theta = theta;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getDbHome() {
        return dbHome;
    }

    public void setDbHome(String dbHome) {
        this.dbHome = dbHome;
    }
}
