package com.corechan.news.service;

import com.corechan.news.common.Status;
import com.corechan.news.dao.DataDao;
import com.corechan.news.dao.UserDao;
import com.corechan.news.entity.Data;
import com.corechan.news.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class DBService {
    private final DataDao dataDao;
    private final UserDao userDao;
    private final DataService dataService;
    private int topics = 0;
    private Long id = 0L;

    @Autowired
    public DBService(DataDao dataDao, UserDao userDao, DataService dataService) {
        this.dataDao = dataDao;
        this.userDao = userDao;
        this.dataService = dataService;
    }

    public Status.StatusCode importNews(String news, String THETA, String img) throws IOException {
        System.out.println("正在删除所有新闻...");
        dataDao.deleteAll();
        List<User> users = userDao.findAll();
        System.out.println("正在重置用户偏好...");
        for (User user : users) {
            user.setPreference(new ArrayList<>());
            userDao.save(user);
        }
        BufferedReader newsReader = new BufferedReader(new InputStreamReader(new FileInputStream(news), "UTF-8"));
        BufferedReader THETAReader = new BufferedReader(new InputStreamReader(new FileInputStream(THETA), "UTF-8"));
        BufferedReader imgReader;
        try {
            imgReader = new BufferedReader(new InputStreamReader(new FileInputStream(img), "UTF-8"));
        } catch (FileNotFoundException e) {
            imgReader = null;
        }

        String newsLine;
        id = 0L;
        System.out.println("从文本文档中导入数据到mongoDB...");
        while ((newsLine = newsReader.readLine()) != null) {
            //读取新闻本体
            if (id % 1000 == 0)
                System.out.println(id);
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
            data.setId(id);
            data.setTitle(title);
            data.setContent(content);
            data.setUrl(url);
            data.setList(list);
            data.setMaxRank(max);
            data.setTopic(topic);
            data.setImg(imgUrl);
            dataDao.save(data);
            id++;
        }
        THETAReader.close();
        newsReader.close();
        if (imgReader != null) {
            imgReader.close();
        }
        System.out.println("共导入" + id + "条数据");
        dataService.init();
        System.out.println("已重置randomTop300Data");
        return Status.StatusCode.success;
    }

    public Status.StatusCode importTopicNews(String DBDataHome, String news, String img) throws IOException {
        Long start = System.currentTimeMillis();
        System.out.println("正在删除所有新闻...");
        dataDao.deleteAll();
        id = 0L;
        List<User> users = userDao.findAll();
        System.out.println("正在重置用户偏好...");
        for (User user : users) {
            user.setPreference(new ArrayList<>());
            userDao.save(user);
        }
        File dataDir = new File(DBDataHome);
        File[] files = dataDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                topics++;
            }
        }
        for (File file : files) {
            if (file.isDirectory()) {
                Integer topic = Integer.parseInt(file.getName().split("-")[0]) - 1;
                importTopic(DBDataHome, file.getName() + "/" + news, file.getName() + "/" + img, topic);
            }
        }
        System.out.println("共导入" + id + "条数据");
        Long end = System.currentTimeMillis();
        System.out.println("共用时：" + (end - start)/1000 + "s");
        dataService.init();
        System.out.println("已重置randomTop300Data");
        return Status.StatusCode.success;
    }

    private void importTopic(String DBDataHome, String news, String img, Integer topic) throws IOException {
        BufferedReader newsReader = new BufferedReader(new InputStreamReader(new FileInputStream(DBDataHome + news), "UTF-8"));
        BufferedReader imgReader = new BufferedReader(new InputStreamReader(new FileInputStream(DBDataHome + img), "UTF-8"));
        String newsLine;

        System.out.println("从" + news + "中导入数据到mongoDB...");
        List<Data> dataList = new ArrayList<>();
        Long cnt = 0L;
        while ((newsLine = newsReader.readLine()) != null) {
            String url = newsLine.split("</url,")[0];
            String title = null;
            String content = null;
            try {
                title = newsLine.split("</contenttitle,")[0].split("</docno,")[1];
                content = newsLine.split("</contenttitle,")[1].split("</content")[0];
            } catch (ArrayIndexOutOfBoundsException e) {
                imgReader.readLine();//此新闻无内容，略过。
                continue;
            }
            List<Double> list = randomList(topic);
            Double max = 0d;
            for (Double rank : list) {
                max = Math.max(rank, max);
            }

            String imgLine = imgReader.readLine();
            String imgUrl = imgLine.split("</contenttitle,")[1].split("</img,")[0];
            if (imgUrl.equals("")) {
                imgUrl = null;
            }
            String date = imgLine.split("</img,")[1].split("</date,")[0];

            Data data = new Data();
            data.setId(id);
            data.setTitle(title);
            data.setContent(content);
            data.setUrl(url);
            data.setList(list);
            data.setMaxRank(max);
            data.setTopic(topic);
            data.setImg(imgUrl);
            data.setDate(date);
            dataList.add(data);

            id++;
            cnt++;
            if (id % 1000 == 0) {
                dataDao.saveAll(dataList);
                System.out.println("已写入" + id + "条新闻");
                dataList.clear();
            }
        }
        dataDao.saveAll(dataList);

        imgReader.close();
        newsReader.close();
        System.out.println("从" + news + "共导入" + cnt + "条数据");
    }

    private List<Double> randomList(Integer topic) {
        Double topicRandomRank = Math.random() * 0.3 + 0.6;
        Double left = 1 - topicRandomRank;
        List<Double> list = new ArrayList<>(Collections.nCopies(topics, 0d));
        list.set(topic, topicRandomRank);
        while (left != 0d) {
            int topicRandomIndex = (int) (Math.random() * topics);
            if (list.get(topicRandomIndex) != 0d) {
                continue;
            }
            Double other = Math.random() * 0.15;
            list.set(topicRandomIndex, Math.min(other, left));
            left -= Math.min(other, left);
        }
        Double sum = 0d;
        for (Double x : list) {
            sum += x;
        }
        return list;
    }
}
