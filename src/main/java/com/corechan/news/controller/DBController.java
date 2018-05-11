package com.corechan.news.controller;

import com.corechan.news.common.Status;
import com.corechan.news.common.config.DBDataConfig;
import com.corechan.news.dao.DataDao;
import com.corechan.news.entity.Data;
import com.corechan.news.service.DBService;
import com.corechan.news.service.StatisticService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

import javax.annotation.PostConstruct;
import javax.servlet.ServletConfig;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@RestController
@ApiIgnore
@RequestMapping("")
public class DBController {
    private final DBService dbService;
    private final StatisticService statisticService;
    private final DBDataConfig dbDataConfig;

    private static String DB_HOME = "/";

    @PostConstruct
    public void init() throws FileNotFoundException {
        DB_HOME = ResourceUtils.getURL(dbDataConfig.getDbHome()).getPath();

    }

    @Autowired
    public DBController(DBService dbService, StatisticService statisticService, DBDataConfig dbDataConfig) {
        this.dbService = dbService;
        this.statisticService = statisticService;
        this.dbDataConfig = dbDataConfig;
    }

    @RequestMapping(value = "/importNewsInto", method = RequestMethod.GET)
    public Status addNews(@RequestParam(defaultValue = "net_news_data.txt") String news,
                          @RequestParam(defaultValue = "THETA.txt") String THETA,
                          @RequestParam(defaultValue = "img.txt") String img,
                          @RequestParam(defaultValue = "rua") String key) throws IOException {
        Status status = new Status();

        if (!key.equals("baishikele")) {//eb6ef96dbd168096345d3a85b56ca4b587bac338906fe89f2738137fae18ae56|baishikele
            status.setStatus(Status.StatusCode.fail);
            status.setMsg("请联系i@corechan.cn 陈睿 李童 阳申湘 江桥并使用密钥导入新闻与训练结果到数据库，tel:15127820236");
            return status;
        }
        status.setStatus(dbService.importInto(DB_HOME + news, DB_HOME + THETA, DB_HOME + img));
        return status;
    }

    @RequestMapping(value = "/statistic", method = RequestMethod.GET)
    public Status statistic(@RequestParam(defaultValue = "rua") String key) {
        Status status = new Status();
        if (key.equals("baishikele")) {
            status.setMsg("统计完成！");
            status.setStatus(statisticService.Statistic());
            return status;
        } else {
            status.setMsg("key不正确，联系i@corechan.cn吧");
            status.setStatus(Status.StatusCode.fail);
            return status;
        }
    }
}
