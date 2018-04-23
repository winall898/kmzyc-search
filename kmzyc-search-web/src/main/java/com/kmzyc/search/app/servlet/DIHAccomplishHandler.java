package com.kmzyc.search.app.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kmzyc.search.app.index.OprationCategoryTask;
import com.kmzyc.search.config.Channel;


/**
 * DIH处理
 * 
 * @author KM
 *
 */
public class DIHAccomplishHandler extends HttpServlet {

    private static final long serialVersionUID = 915075464342772144L;

    private static final Logger LOG = LoggerFactory.getLogger(DIHAccomplishHandler.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        LOG.info("接收到执行产品运营类目生成任务的命令。");

        // 运营类目的生成
        new Thread(new OprationCategoryTask(Channel.b2b)).start();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        doGet(req, resp);
    }

}
