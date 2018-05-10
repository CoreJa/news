package com.corechan.news.controller;

import com.corechan.news.common.Status;
import com.corechan.news.entity.Statistic;
import com.corechan.news.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/statisticList")
@CrossOrigin(allowCredentials = "true")
public class StatisticController {
    private final StatisticService statisticService;

    @Autowired
    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    Status<List<Statistic>> getStatisticList() {
        Status<List<Statistic>> status = new Status<>();
        status.setData(statisticService.getStatisticList());
        status.setStatus(Status.StatusCode.success);
        return status;
    }

    @RequestMapping(value = "/{topic}", method = RequestMethod.GET)
    Status<Statistic> getStatistic(@PathVariable Integer topic) {
        Status<Statistic> status = new Status<>();
        status.setData(statisticService.getStatistic(topic));
        status.setStatus(Status.StatusCode.success);
        return status;
    }
}
