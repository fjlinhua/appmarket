/*
 * Copyright (c) 2005, 2015, EGENIUSS Technology Co.,Ltd. All rights reserved.
 * EGENIUSS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.egeniuss.appmarket.controller;

import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.egeniuss.appmarket.util.DesUtil;

/**
 * <p>
 * Title: AppServiceController.java
 * </p>
 * Description:
 * <p>
 * Modify histoty:
 * 
 * @author Linhua
 * @version 1.0
 * @created 2015年8月13日 下午4:23:34
 **/
@RequestMapping("/")
@Controller
public class AppServiceController {

    private JdbcTemplate jdbcTemplate;
    @Value("${server.url.prefix}")
    private String urlPrefix;

    @SuppressWarnings("rawtypes")
    @RequestMapping("/queryAppVersionService.html")
    public @ResponseBody Map<String, Object> queryAppVersionService(HttpServletRequest request) {
        String conditions = request.getParameter("conditions");
        Map<String, Object> msg = new HashMap<String, Object>();
        if (conditions == null || "".equals(conditions)) {
            msg.put("errcode", -1);
            msg.put("errmsg", "service parameters is null");
            return msg;
        }
        String jsonData = null;
        try {
            jsonData = DesUtil.decrypt(conditions, "87555457");
        } catch (Exception e) {
            msg.put("errcode", -2);
            msg.put("errmsg", "key error");
            return msg;
        }
        Map params = null;
        try {
            params = JSON.parseObject(jsonData, Map.class);
        } catch (Exception e) {
            msg.put("errcode", -2);
            msg.put("errmsg", "key error");
            return msg;
        }
        String appKey = (String) params.get("APP_KEY");
        String releaseType = (String) params.get("RELEASE_TYPE");
        if (appKey == null || "".equals(appKey) || releaseType == null || "".equals(releaseType)) {
            msg.put("errcode", -3);
            msg.put("errmsg", "query parameters is null");
            return msg;
        }
        String appPlatform = (String) params.get("APP_PLATFORM");
        if (appPlatform == null || "".equals(appPlatform)) {
            appPlatform = "Android";
        }
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from APP_MARKET where APP_PLATFORM = ? and APP_ENV = ? and APP_KEY = ? ORDER BY CREATE_TIME DESC LIMIT 0,1", new Object[] { appPlatform, releaseType, appKey }, new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR });
        Map<String, Object> appVersion = null;
        if (list == null || list.size() == 0) {
            appVersion = new HashMap<String, Object>();
        } else {
            appVersion = list.get(0);
            String appUrl = urlPrefix + appVersion.get("FILE_NAME");
            appVersion.put("URL", appUrl);
        }
        msg.put("errcode", 0);
        msg.put("errmsg", appVersion);
        return msg;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }
}
