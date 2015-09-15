/*
 * Copyright (c) 2005, 2015, EGENIUSS Technology Co.,Ltd. All rights reserved.
 * EGENIUSS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.egeniuss.appmarket.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * Title: AppManagerController.java
 * </p>
 * Description:
 * <p>
 * Modify histoty:
 * 
 * @author Linhua
 * @version 1.0
 * @created 2015-4-14 下午4:07:59
 **/
@RequestMapping("/")
@Controller
public class AppManagerController {

    private static final Logger LOGGER = Logger.getLogger(AppManagerController.class);
    private JdbcTemplate jdbcTemplate;
    @Value("${app.store.path}")
    private String appStore;

    @RequestMapping("/listApp.html")
    public @ResponseBody List<Map<String, Object>> listApp(HttpServletRequest request) {
        StringBuilder sql = new StringBuilder("select am.*, ak.APP_NAME, ar.RELEASE_NAME from APP_MARKET am, APP_KEY_MAPPING ak, APP_RELEASE_MAPPING ar where am.APP_KEY = ak.APP_KEY and am.APP_ENV = ar.RELEASE_KEY");
        List<Object> params = new ArrayList<Object>();
        List<Integer> types = new ArrayList<Integer>();
        String appKey = request.getParameter("appKey");
        if (appKey != null && !"".equals(appKey)) {
            sql.append(" and am.APP_KEY = ?");
            params.add(appKey);
            types.add(Types.VARCHAR);
        }
        String appPlatform = request.getParameter("appPlatform");
        if (appPlatform != null && !"".equals(appPlatform)) {
            sql.append(" and am.APP_PLATFORM = ?");
            params.add(appPlatform);
            types.add(Types.VARCHAR);
        }
        String appEnv = request.getParameter("appEnv");
        if (appEnv != null && !"".equals(appEnv)) {
            sql.append(" and am.APP_ENV = ?");
            params.add(appEnv);
            types.add(Types.VARCHAR);
        }
        sql.append(" order by am.CREATE_TIME desc");
        List<Map<String, Object>> list = null;
        if (params.size() == 0) {
            list = jdbcTemplate.queryForList(sql.toString());
        } else {
            int[] sqlTypes = new int[types.size()];
            for (int i = 0; i < types.size(); i++) {
                sqlTypes[i] = types.get(i);
            }
            list = jdbcTemplate.queryForList(sql.toString(), params.toArray(), sqlTypes);
        }
        return list;
    }

    @RequestMapping("/queryAppKeySelector.html")
    public @ResponseBody List<Map<String, Object>> queryAppKeySelector() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from APP_KEY_MAPPING");
        return list;
    }

    @RequestMapping("/queryAppReleaseSelector.html")
    public @ResponseBody List<Map<String, Object>> queryAppReleaseSelector() {
        List<Map<String, Object>> list = jdbcTemplate.queryForList("select * from APP_RELEASE_MAPPING");
        return list;
    }

    @RequestMapping("/uploadApp.html")
    public @ResponseBody Map<String, Object> uploadApp(@RequestParam(required = false) MultipartFile app, HttpServletRequest request) {
        Date now = new Date();
        long time = now.getTime();
        String appFileName = app.getOriginalFilename();
        String realFileName = time + appFileName.substring(appFileName.lastIndexOf("."));
        Map<String, Object> msg = new HashMap<String, Object>();
        try {
            FileUtils.copyInputStreamToFile(app.getInputStream(), new File(appStore.concat(realFileName)));
        } catch (IOException e) {
            LOGGER.error("文件写入磁盘失败", e);
            msg.put("errcode", -1);
            msg.put("errmsg", "版本上传失败：<br/>" + e.getMessage());
            return msg;
        }
        SimpleDateFormat formator = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        StringTokenizer linemsg = new StringTokenizer(request.getParameter("releaseNote"), "\n");
        StringBuilder releaseNoteSb = new StringBuilder();
        while (linemsg.hasMoreElements()) {
            releaseNoteSb.append(linemsg.nextElement()).append("\\n");
        }
        Object[] args = new Object[] { time, request.getParameter("appKey"), request.getParameter("appPlatform"), request.getParameter("appEnv"), request.getParameter("versionNum"), app.getSize(), releaseNoteSb.toString(), realFileName, formator.format(now) };
        int[] argTypes = new int[] { Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.DECIMAL, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR };
        jdbcTemplate.update("insert into APP_MARKET (APP_ID, APP_KEY, APP_PLATFORM, APP_ENV, VERSION_NUM, APP_SIZE, RELEASE_NOTE, FILE_NAME, CREATE_TIME) values (?, ?, ?, ?, ?, ?, ?, ?, ?)", args, argTypes);
        msg.put("errcode", 0);
        msg.put("errmsg", "ok");
        return msg;
    }

    @RequestMapping("/deleteAppVersion.html")
    public @ResponseBody Map<String, Object> deleteAppVersion(@RequestParam(value = "appIds") String appIds) {
        Map<String, Object> msg = new HashMap<String, Object>();
        if (appIds == null || "".equals(appIds)) {
            msg.put("errcode", -1);
            msg.put("errmsg", "请选择待删除的版本");
            return msg;
        }
        List<Object[]> batchArgs = new ArrayList<Object[]>();
        String[] appId = appIds.split(",");
        for (String oneAppId : appId) {
            Object[] p = new Object[] { oneAppId };
            batchArgs.add(p);
        }
        jdbcTemplate.batchUpdate("delete from APP_MARKET where APP_ID = ?", batchArgs, new int[] { Types.VARCHAR });
        msg.put("errcode", 0);
        msg.put("errmsg", "ok");
        return msg;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

}
