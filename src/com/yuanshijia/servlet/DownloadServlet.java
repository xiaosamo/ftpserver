package com.yuanshijia.servlet;

import com.yuanshijia.db.DerbyDb;
import com.yuanshijia.entity.FileInfo;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
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
@WebServlet("/download")
public class DownloadServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String uuid = request.getParameter("uuid");
        FileInfo fileInfo = DerbyDb.queryById(uuid);
        if (fileInfo == null) {
            // 文件不存在
            response.setStatus(410);
            return;
        }

        response.setCharacterEncoding("utf-8");
        response.setHeader("Content-Disposition", "attachment; filename=" + fileInfo.getUuid() + fileInfo.getType());


        InputStream is = new FileInputStream(new File(fileInfo.getSavePath()));
        ServletOutputStream os = response.getOutputStream();

        byte[] data = new byte[1024];
        int len;
        while ((len = is.read(data)) > 0) {
            os.write(data, 0, len);
        }

        os.close();
    }
}
