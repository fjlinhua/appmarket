/*
 * Copyright (c) 2005, 2015, EGENIUSS Technology Co.,Ltd. All rights reserved.
 * EGENIUSS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.egeniuss.appmarket.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * <p>
 * Title: LoginController.java
 * </p>
 * Description:
 * <p>
 * Modify histoty:
 * 
 * @author Linhua
 * @version 1.0
 * @created 2015年6月10日 上午10:52:20
 **/
@Controller
public class LoginController {

    @RequestMapping("/login.html")
    public String login(@RequestParam(value = "userName", required = false) String userName, @RequestParam(value = "password", required = false) String password, HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        if (request.getMethod().equalsIgnoreCase("GET")) {
            model.addAttribute("userName", userName);
            model.addAttribute("password", password);
            return "login";
        }
        if (userName == null || "".equals(userName) || password == null || "".equals(password)) {
            model.addAttribute("userName", userName);
            model.addAttribute("password", password);
            model.addAttribute("errorMsg", "用户名或密码为空");
            return "login";
        }
        if ("appadmin".equals(userName) && "admin87555457".equals(password)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", userName + "@" + password);
            return "redirect:index.html";
        } else {
            model.addAttribute("userName", userName);
            model.addAttribute("password", password);
            model.addAttribute("errorMsg", "用户名或密码错误");
            return "login";
        }
    }

}
