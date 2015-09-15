/*
 * Copyright (c) 2005, 2015, EGENIUSS Technology Co.,Ltd. All rights reserved.
 * EGENIUSS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.egeniuss.appmarket.web;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * <p>
 * Title: LoginFilter.java
 * </p>
 * Description:
 * <p>
 * Modify histoty:
 * 
 * @author Linhua
 * @version 1.0
 * @created 2015年6月10日 上午10:46:04
 **/
public class LoginFilter implements Filter {

    private static final String USER = "appadmin@admin87555457";

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        if (req.getRequestURI().contains("queryAppVersionService")) {
            chain.doFilter(req, resp);
            return;
        }
        HttpSession session = req.getSession();
        String user = (String) session.getAttribute("user");
        if (USER.equals(user)) {// 如果用户已经登录
            if (req.getRequestURI().contains("/login.html")) {// 如果执行登录请求，直接跳转到首页
                resp.sendRedirect("index.html");
            } else {// 否则执行相应的请求
                chain.doFilter(req, resp);
            }
        } else {// 如果用户没有登录
            if (req.getRequestURI().contains("/login.html")) {// 如果是登录请求，则执行登录请求
                chain.doFilter(req, resp);
            } else {// 否则跳转到登录页面
                resp.sendRedirect("login.html");
            }
        }
    }

    @Override
    public void init(FilterConfig config) throws ServletException {

    }

}
