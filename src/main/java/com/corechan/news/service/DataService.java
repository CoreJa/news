package com.corechan.news.service;

import com.corechan.news.dao.DataDao;
import com.corechan.news.entity.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.connection.SortParameters;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;

@Service
public class DataService {
    private final DataDao dataDao;
    private static List<Data> randomTop300Data = null;

    @Autowired
    public DataService(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public List<Data> data(Integer page, Integer size) {
        List<Data> datas = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            datas.addAll(topicData(i, page, size));
        }
        return datas;
    }

    @PostConstruct
    public void init() {
        Sort sort = new Sort(Sort.Direction.DESC, "maxRank");
        Pageable pageable = PageRequest.of(0, 300, sort);
        randomTop300Data = dataDao.findAll(pageable).getContent();
    }

    public List<Data> randomData(Integer number) {
        Set<Integer> randoms = new HashSet<>();
        List<Data> dataList = new ArrayList<>();
        while (randoms.size() < number) {
            Integer random = (int) (Math.random() * randomTop300Data.size());
            if (!randoms.contains(random)) {
                randoms.add(random);
                Data data = randomTop300Data.get(random);
                int cutSize = 100;
                cutSize = Math.min(data.getContent().length(), cutSize);
                data.setContent(data.getContent().substring(0, cutSize));
                dataList.add(data);
            }
        }
        return dataList;
    }

    public List<Data> topicData(Integer topic, Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        List<Data> dataList = dataDao.findByTopicOrderByMaxRankDesc(topic, pageable).getContent();
        List<Data> returnDataList = new ArrayList<>();
        for (Data data : dataList) {
            int cutSize = 100;
            cutSize = Math.min(data.getContent().length(), cutSize);
            data.setContent(data.getContent().substring(0, cutSize));
            returnDataList.add(data);
        }
        return returnDataList;
    }

    public Data getData(Integer newsId) {
        if (dataDao.findById(newsId).isPresent()) {
            return dataDao.findById(newsId).get();
        } else return null;
    }
}
