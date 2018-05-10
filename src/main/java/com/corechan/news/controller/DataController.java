package com.corechan.news.controller;

import com.corechan.news.common.Status;
import com.corechan.news.entity.Data;
import com.corechan.news.service.DataService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/data")
public class DataController {
    private final DataService dataService;

    @Autowired
    public DataController(DataService dataService) {
        this.dataService = dataService;
    }

    @ApiOperation(value ="安卓端总数据获取接口",notes = "新增page与size参数，均有默认值。数据按照分类效果排序。默认本地测试请求约1s，每次请求约100kb+")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "0",name = "page",value = "page为分页，建议每次请求时+1"),
            @ApiImplicitParam(defaultValue = "10",name = "size",value = "size为数据大小，默认10")
    })
    @RequestMapping(value = "/android", method = RequestMethod.GET)
    public Status<List<Data>> androidData(@RequestParam(defaultValue = "0") Integer page,
                                          @RequestParam(defaultValue = "20") Integer size) {
        Status<List<Data>> status = new Status<>();
        status.setData(dataService.data(page, size));
        status.setStatus(Status.StatusCode.success);
        return status;
    }
    @ApiOperation(value ="安卓端分类数据获取接口",notes = "新增page与size参数，均有默认值。数据按照分类效果排序。默认测试单个请求很快，约15kb+")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "0",name = "page",value = "page为分页，建议每次请求时+1"),
            @ApiImplicitParam(defaultValue = "20",name = "size",value = "size为数据大小，默认20"),
            @ApiImplicitParam(name = "topic",value = "主题分类，0~9")
    })
    @RequestMapping(value = "/android/{topic}", method = RequestMethod.GET)
    public Status<List<Data>> androidTopicData(@PathVariable Integer topic,
                                               @RequestParam(defaultValue = "0") Integer page,
                                               @RequestParam(defaultValue = "20") Integer size) {
        Status<List<Data>> status = new Status<>();
        status.setData(dataService.topicData(topic, page, size));
        status.setStatus(Status.StatusCode.success);
        return status;
    }
    @ApiOperation(value ="web端随机数据获取接口",notes = "获取随机新闻，每次默认20条")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "20",name = "size",value = "size为数据大小，默认20")
    })
    @RequestMapping(value = "/web", method = RequestMethod.GET)
    public Status<List<Data>> webData(@RequestParam(defaultValue = "20") Integer size) {
        Status<List<Data>> status = new Status<>();
        status.setData(dataService.randomData(size));
        for (Data data : status.getData()) {
            data.setUrl(data.getUrl().replaceAll(".com/m/", ".com/"));
        }
        status.setStatus(Status.StatusCode.success);
        return status;
    }
    @ApiOperation(value ="web端分类数据获取接口",notes = "新增page与size参数，均有默认值。数据按照分类效果排序。默认测试单个请求很快，约15kb+")
    @ApiImplicitParams({
            @ApiImplicitParam(defaultValue = "0",name = "page",value = "page为分页，建议每次请求时+1"),
            @ApiImplicitParam(defaultValue = "20",name = "size",value = "size为数据大小，默认20"),
            @ApiImplicitParam(name = "topic",value = "主题分类，0~9")
    })
    @RequestMapping(value = "/web/{topic}", method = RequestMethod.GET)
    public Status<List<Data>> webTopicData(@PathVariable Integer topic,
                                           @RequestParam(defaultValue = "0") Integer page,
                                           @RequestParam(defaultValue = "20") Integer size) {
        Status<List<Data>> status = new Status<>();
        status.setData(dataService.topicData(topic, page, size));
        for (Data data : status.getData()) {
            data.setUrl(data.getUrl().replaceAll(".com/m/", ".com/"));
        }
        status.setStatus(Status.StatusCode.success);
        return status;
    }
}
