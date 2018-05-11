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
    private Double maxPreferenceRank;//这个值每个用户访问接口时只修改一次。后续不修改。
    private boolean maxPreferenceRankInit = true;

    @Autowired
    public RecommendationService(UserService userService, DataService dataService) {
        this.userService = userService;
        this.dataService = dataService;
        this.recommendationData = new ArrayList<>();
    }

    public List<Data> getRecommendation(String userId, Integer page, Integer size) {
        List<Double> preference = userService.showUser(userId).getPreference();
        if (preference == null || preference.size() == 0)
            return null;
        Integer dataPage = page / 10;
        //每次从数据库里找200个推荐项，但每次只发20个。如果200个发完了那就再刷后200个
        if (page % 10 == 0) {
            recommendationData = dataService.data(dataPage, 20);
            for (Data data : recommendationData) {
                data.setPreferenceRank(data.getMaxRank() * preference.get(data.getTopic()));
            }
            Collections.sort(recommendationData);
            if (maxPreferenceRankInit) {
                maxPreferenceRank = recommendationData.get(0).getPreferenceRank();
                maxPreferenceRankInit = false;
            }
            for (Data data : recommendationData) {
                data.setPreferenceRank(data.getPreferenceRank() / maxPreferenceRank);
                int cutSize = 100;
                cutSize = Math.min(data.getContent().length(), cutSize);
                data.setContent(data.getContent().substring(0, cutSize));
            }
        }
        page %= 10;
        return recommendationData.subList(page * size, (page + 1) * size);
    }
}
