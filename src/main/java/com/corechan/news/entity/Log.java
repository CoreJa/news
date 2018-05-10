package com.corechan.news.entity;

public class Log extends Data{
    private Long time;

    public Log() {
    }

    public Log(Data data) {
        super(data.getId(), data.getList(), data.getTitle(),data.getContent(),data.getUrl(),data.getImg(),data.getTopic(),data.getMaxRank(),data.getPreferenceRank());
        this.time = System.currentTimeMillis();
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
