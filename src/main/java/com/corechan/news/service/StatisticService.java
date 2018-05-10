package com.corechan.news.service;

import com.corechan.news.common.Status;
import com.corechan.news.dao.DataDao;
import com.corechan.news.dao.StatisticDao;
import com.corechan.news.entity.Data;
import com.corechan.news.entity.Statistic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class StatisticService {
    private final DataDao dataDao;
    private final StatisticDao statisticDao;

    @Autowired
    public StatisticService(DataDao dataDao, StatisticDao statisticDao) {
        this.dataDao = dataDao;
        this.statisticDao = statisticDao;
    }

    public Status.StatusCode Statistic() {
        statisticDao.deleteAll();
        List<Statistic> statisticList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            statisticList.add(new Statistic());
        }
        System.out.println("开始统计新闻类别...");
        for (Integer i = 0; i < dataDao.count(); i++) {
            if (i % 1000 == 0) {
                System.out.println("已统计" + i + "条新闻...");
            }
            Data data = dataDao.findById(i).get();
            String topic = data.getUrl().substring(27).split("/")[0];
            Statistic statistic = statisticList.get(data.getTopic());
            Map<String, Integer> map = statistic.getTopic();
            if (!map.containsKey(topic))
                map.put(topic, 1);
            else {
                map.put(topic, map.get(topic) + 1);
            }
            statistic.setTopic(map);
            statisticList.set(data.getTopic(), statistic);
        }
        System.out.println("统计结束！");
        for (Statistic statistic : statisticList) {
            String maxCountTopic = null;
            Integer maxcount = 0;
            for (String key : statistic.getTopic().keySet()) {
                Integer count = statistic.getTopic().get(key);
                if (maxcount < count) {
                    maxcount = count;
                    maxCountTopic = key;
                }
            }
            statistic.setId((long) statisticList.indexOf(statistic));
            statistic.setMaxCountTopic(maxCountTopic);
            statisticDao.save(statistic);
        }
        return Status.StatusCode.success;
    }

    public List<Statistic> getStatisticList() {
        return statisticDao.findAll();
    }

    public Statistic getStatistic(Integer topic) {
        if (statisticDao.findById(topic).isPresent())
            return statisticDao.findById(topic).get();
        else
            return null;
    }
}
