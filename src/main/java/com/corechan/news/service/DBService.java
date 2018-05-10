package com.corechan.news.service;

import com.corechan.news.common.Status;
import com.corechan.news.dao.DataDao;
import com.corechan.news.entity.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBService {
    private final DataDao dataDao;

    @Autowired
    public DBService(DataDao dataDao) {
        this.dataDao = dataDao;
    }

    public Status.StatusCode importInto(String news, String THETA, String img) throws IOException {
        dataDao.deleteAll();

        BufferedReader newsReader = new BufferedReader(new InputStreamReader(new FileInputStream(news), "UTF-8"));
        BufferedReader THETAReader = new BufferedReader(new InputStreamReader(new FileInputStream(THETA), "UTF-8"));
        BufferedReader imgReader;
        try {
            imgReader = new BufferedReader(new InputStreamReader(new FileInputStream(img), "UTF-8"));
        } catch (FileNotFoundException e) {
            imgReader = null;
        }

        String newsLine;
        Long cnt = 0L;
        System.out.println("从文本文档中导入数据到mongoDB...");
        while ((newsLine = newsReader.readLine()) != null) {
            //读取新闻本体
            if (cnt % 1000 == 0)
                System.out.println(cnt);
            String url_raw = newsLine.split("</url,")[0];
            String url = url_raw.replaceAll("com//", "com/m/");
            String title = null;
            String content = null;
            try {
                title = newsLine.split("</contenttitle,")[0].split("</docno,")[1];
                content = newsLine.split("</contenttitle,")[1].split("</content")[0];
            } catch (ArrayIndexOutOfBoundsException e) {

            }
            //处理该条新闻得分情况
            List<Double> list = new ArrayList<>();
            String THETALine = THETAReader.readLine();
            if (THETALine == null) {
                break;
            }
            String[] THETAs = THETALine.split(" ");
            double max = 0;
            double rank = 0;
            int topic = -1;
            for (int i = 0; i < THETAs.length; i++) {
                rank = Double.parseDouble(THETAs[i]);
                list.add(rank);
                max = max < rank ? rank : max;
            }
            for (int i = 0; i < THETAs.length; i++) {
                if (Double.parseDouble(THETAs[i]) == max) {
                    topic = i;
                    break;
                }
            }

            //读取新闻图片
            String imgSrc = null;
            String imgUrl = null;
            if (imgReader != null) {
                String imgLine = null;
                do {
                    //不匹配数据，img往下读
                    imgLine = imgReader.readLine();
                    imgSrc = imgLine.split("<url,")[0];
                } while (!url_raw.equals(imgSrc));
                imgUrl = imgLine.split("<url,")[1].split("<im")[0];
                if (imgUrl.equals(""))
                    imgUrl = null;
                if (imgUrl != null && !imgUrl.startsWith("http")) {
                    imgUrl = url.substring(0, url.lastIndexOf("/")) + "/" + imgUrl;
                }
            }
            Data data = new Data();
            data.setId(cnt);
            data.setTitle(title);
            data.setContent(content);
            data.setUrl(url);
            data.setList(list);
            data.setMaxRank(max);
            data.setTopic(topic);
            data.setImg(imgUrl);
            dataDao.save(data);
            cnt++;
        }
        THETAReader.close();
        newsReader.close();
        if (imgReader != null) {
            imgReader.close();
        }
        System.out.println("共导入" + cnt + "条数据");
        return Status.StatusCode.success;
    }
}