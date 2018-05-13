package com.corechan.news.service;

import com.corechan.news.common.Status;
import com.corechan.news.common.util.BCryptUtil;
import com.corechan.news.dao.UserDao;
import com.corechan.news.entity.Data;
import com.corechan.news.entity.Log;
import com.corechan.news.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserService {
    private final UserDao userDao;
    private final DataService dataService;

    @Autowired
    public UserService(UserDao userDao, DataService dataService) {
        this.userDao = userDao;
        this.dataService = dataService;
    }

    public Status.StatusCode register(User user) {
        if (getUser(user.getUserId()) == null) {
            user.setPassword(BCryptUtil.encode(user.getPassword()));
            userDao.save(user);
            return Status.StatusCode.success;
        } else return Status.StatusCode.idAlreadyExist;
    }

    public Status.StatusCode login(String userId, String password) {
        if (getUser(userId) != null) {
            if (BCryptUtil.match(password, getUser(userId).getPassword())) {
                return Status.StatusCode.success;
            } else {
                return Status.StatusCode.wrongPassword;
            }
        } else {
            return Status.StatusCode.idNotExist;
        }
    }

    public User showUser(String userId) {
        User user = getUser(userId);
        if (user == null) {
            return null;
        }
        //清洗数据
        user.setPassword(null);
        //清洗偏好
        int count = user.getHistory().size();
        List<Double> newPreference = new ArrayList<>();
        for (double point : user.getPreference()) {
            newPreference.add(point / count);
        }
        user.setPreference(newPreference);
        //倒序输出历史
        List<Log> newHistory = new ArrayList<>();
        List<Log> history = user.getHistory();
        for (int i = history.size() - 1; i >= 0; i--) {
            newHistory.add(history.get(i));
        }
        user.setHistory(newHistory);
        return user;
    }

    private User getUser(String userId) {
        if (userDao.findById(userId).isPresent())
            return userDao.findById(userId).get();
        else return null;
    }

    private Status.StatusCode update(User user) {
        userDao.save(user);
        return Status.StatusCode.success;
    }

    public Status.StatusCode postLog(String userId, Integer newsId) {
        if (dataService.getData(newsId) == null) {
            return Status.StatusCode.newsNotExist;
        }
        Log log = new Log(dataService.getData(newsId));
        User user = getUser(userId);
        user.getHistory().add(log);
        user.setPreference(updatePreference(userId, newsId));
        return update(user);
    }

    private List<Double> updatePreference(String userId, Integer newsId) {
        User user = getUser(userId);
        List<Double> preference = user.getPreference();
        List<Double> rank = dataService.getData(newsId).getList();
        //如果用户偏好为空，则设置为查看的第一篇新闻的rank
        if (preference.size() == 0) {
            return rank;
        } else {
            for (int i = 0; i < rank.size(); i++) {
                preference.set(i, preference.get(i) + rank.get(i));
            }
            return preference;
        }
    }
}
