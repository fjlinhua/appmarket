/*
 * Copyright (c) 2005, 2015, EGENIUSS Technology Co.,Ltd. All rights reserved.
 * EGENIUSS PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 */
package com.egeniuss.appmarket.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * <p>
 * Title: IndexController.java
 * </p>
 * Description:
 * <p>
 * Modify histoty:
 * 
 * @author Linhua
 * @version 1.0
 * @created 2015年6月4日 下午6:00:07
 **/
@Controller
public class IndexController {

    @RequestMapping("/index.html")
    public String index(Model model) {
        return "index";
    }

}
