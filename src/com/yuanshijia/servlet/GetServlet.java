package com.yuanshijia.servlet;

import com.alibaba.fastjson.JSON;
import com.yuanshijia.db.DerbyDb;
import com.yuanshijia.entity.FileInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * @author yuan
 * @date 2019/10/21
 * @description
 */
@WebServlet("/get")
public class GetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("utf-8");
        response.setHeader("content-type","text/html;charset=UTF-8");
        PrintWriter writer = response.getWriter();

        String uuid = request.getParameter("uuid");
        if (uuid == null) {
            // 获取全部数据
            writer.write(JSON.toJSONString(DerbyDb.queryAll()));
        } else {
            // 获取一条数据
            FileInfo fileInfo = DerbyDb.queryById(uuid);
            if (fileInfo != null) {
                writer.write(JSON.toJSONString(fileInfo));
            } else {
                writer.write("{}");
            }
        }
        writer.flush();
    }
}
