package com.corechan.news.service;

import com.corechan.news.common.Status;
import com.corechan.news.entity.Data;
import com.corechan.news.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {
    private final UserService userService;
    private final DataService dataService;

    @Autowired
    public RecommendationService(UserService userService, DataService dataService) {
        this.userService = userService;
        this.dataService = dataService;
    }

    public List<Data> getRecommendation(String userId,Integer page) {
        List<Double> preference = userService.showUser(userId).getPreference();
        if (preference == null || preference.size() == 0)
            return null;

        List<Data> dataList = dataService.data(page,30);
        for (Data data : dataList) {
            data.setPreferenceRank(data.getMaxRank() * preference.get(data.getTopic()));
        }
        Collections.sort(dataList);
        Double maxPreferenceRank = dataList.get(0).getPreferenceRank();
        for (Data data : dataList) {
            data.setPreferenceRank(data.getPreferenceRank() / maxPreferenceRank);
        }
        return dataList;
    }
}
