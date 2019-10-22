package com.yuanshijia;

import com.yuanshijia.db.DerbyDb;
import com.yuanshijia.filter.ServerFilter;
import com.yuanshijia.servlet.DownloadServlet;
import com.yuanshijia.servlet.GetServlet;
import com.yuanshijia.servlet.UploadServlet;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.DispatcherType;
import javax.servlet.MultipartConfigElement;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.EnumSet;

/**
 * @author yuan
 * @date 2019/10/22
 * @description
 */
public class Main {


    public static void main(String[] args) {
        Server server = new Server(8080);

        try {

            ServletContextHandler context = new ServletContextHandler(
                    ServletContextHandler.SESSIONS);
            // 根路径
            context.setContextPath("/ftp-server");

            // 添加servlet
            context.addServlet(new ServletHolder(GetServlet.class), "/get");
            context.addServlet(new ServletHolder(DownloadServlet.class), "/download");

            ServletHolder servletHolder = new ServletHolder(UploadServlet.class);
            servletHolder.getRegistration().setMultipartConfig(new MultipartConfigElement((String) null));
            context.addServlet(servletHolder, "/upload");

            // 添加filter
            context.addFilter(new FilterHolder(ServerFilter.class), "/*", EnumSet.of(DispatcherType.REQUEST));
            server.setHandler(context);

            // 启动 Server
            server.start();
            System.err.println("server start.");

            // 启动derby
            DerbyDb.create();

            server.join();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                // 最后关闭数据库
                DriverManager.getConnection("jdbc:derby:ftpserver;shutdown=true");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
