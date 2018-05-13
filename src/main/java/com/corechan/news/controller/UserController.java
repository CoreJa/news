package com.corechan.news.controller;


import com.corechan.news.common.Status;
import com.corechan.news.common.exceptions.UnLoginException;
import com.corechan.news.entity.User;
import com.corechan.news.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.support.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@CrossOrigin(allowCredentials = "true")
@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    Status<User> getUser(HttpSession httpSession) {
        String userId = (String) httpSession.getAttribute("userId");
        if (userId == null) {
            throw new UnLoginException();
        }
        Status<User> status = new Status<>();
        status.setStatus(Status.StatusCode.success);
        status.setData(userService.showUser(userId));
        return status;
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    Status login(@RequestParam String userId, @RequestParam String password, HttpServletRequest request) {
        HttpSession session =request.getSession();
        Status status = new Status<>();
        status.setStatus(userService.login(userId, password));
        if (status.getStatus() == Status.StatusCode.success) {
            if(!session.isNew()){
                session.invalidate();
                session=request.getSession(true);
            }
            session.setAttribute("userId", userId);
            session.setMaxInactiveInterval(24 * 3600 * 7);
        }
        return status;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    Status register(@RequestParam String userId, @RequestParam String password, @RequestParam String name) {
        Status status = new Status();
        User user = new User(userId, password, name);
        status.setStatus(userService.register(user));
        return status;
    }

    @RequestMapping(value = "/logout", method = RequestMethod.POST)
    Status logout(HttpSession session) {
        Status status = new Status();
        if (session.getAttribute("userId") == null) {
            throw new UnLoginException();
        }
        session.removeAttribute("userId");
        session.setMaxInactiveInterval(0);
        session.invalidate();
        status.setStatus(Status.StatusCode.success);
        return status;
    }

    @RequestMapping(value = "/log", method = RequestMethod.POST)
    Status postLog(@RequestParam Integer newsId, HttpSession session) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null)
            throw new UnLoginException();
        Status status = new Status();
        status.setStatus(userService.postLog(userId, newsId));
        return status;
    }
}
