package com.corechan.news.controller;

import com.corechan.news.common.Status;
import com.corechan.news.common.exceptions.UnLoginException;
import com.corechan.news.entity.Data;
import com.corechan.news.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@CrossOrigin(allowCredentials = "true")
@RequestMapping("/recommendation")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @Autowired
    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public Status<List<Data>> getRecommendation(HttpSession session,
                                                @RequestParam(defaultValue = "0") Integer page,
                                                @RequestParam(defaultValue = "20") Integer size) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            throw new UnLoginException();
        Status<List<Data>> status = new Status<>();
        status.setData(recommendationService.getRecommendation(userId, page, size));
        if (status.getData() == null) {
            status.setStatus(Status.StatusCode.preferenceNotExist);
            return status;
        }
        status.setStatus(Status.StatusCode.success);
        return status;
    }
}
