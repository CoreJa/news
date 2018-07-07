package com.corechan.news.service;

import com.corechan.news.entity.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RecommendationService {
    private final UserService userService;
    private final DataService dataService;

    private List<Data> recommendationData;
    private List<Double> oldPreference;

    @Autowired
    public RecommendationService(UserService userService, DataService dataService) {
        this.userService = userService;
        this.dataService = dataService;
        this.oldPreference = new ArrayList<>();
        //每次从数据库里找20*topics个推荐项，但每次只发20个。
        this.recommendationData = dataService.data(0, 100);
    }

    public List<Data> getRecommendation(String userId, Integer page, Integer size) {
        List<Double> preference = userService.showUser(userId).getPreference();
        if (preference == null || preference.size() == 0) {
            return null;
        }
        if (!preference.equals(oldPreference)) {
            oldPreference = preference;
            for (Data data : recommendationData) {
                data.setPreferenceRank(data.getMaxRank() * preference.get(data.getTopic()));
            }
            Collections.sort(recommendationData);
            Double maxPreferenceRank = recommendationData.get(0).getPreferenceRank();

            for (Data data : recommendationData) {
                data.setPreferenceRank(data.getPreferenceRank() / maxPreferenceRank);
            }
        }
        return recommendationData.subList(page * size, (page + 1) * size);
    }
}
