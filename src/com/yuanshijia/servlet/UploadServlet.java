package com.yuanshijia.servlet;

import com.yuanshijia.db.DerbyDb;
import com.yuanshijia.entity.FileInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static com.yuanshijia.Constants.SAVE_PATH;

/**
 * @author yuan
 * @date 2019/10/18
 * @description
 */
@MultipartConfig
@WebServlet("/upload")
public class UploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String contentType = request.getContentType();

            //获取上传的文件
            Part part = request.getPart("file");

            //获取请求的信息
            String name = part.getHeader("content-disposition");
            System.out.println(name);

            // 文件大小
            long fileSize = part.getSize();
            // 文件名称
            String fileName = part.getSubmittedFileName();
            // 文件类型
//            String fileType = part.getContentType();

            //获取上传文件的目录
            String root = request.getServletContext().getRealPath("/upload");
            System.out.println("测试上传文件的路径：" + root);


            //获取文件的后缀
            String suffix = fileName.substring(fileName.lastIndexOf("."));
//            System.out.println("测试获取文件的后缀：" + suffix);

            //生成新文件名
            String uuidName = UUID.randomUUID().toString();

            // 保存到指定文件
            String destPath = save(part, uuidName,suffix);

            // 保存到数据库
            FileInfo fileInfo = new FileInfo(uuidName, fileSize, suffix, fileName, new Date(), destPath);
            DerbyDb.add(fileInfo);

//            System.out.println("生成的文件名：" + uuidName);


            PrintWriter writer = response.getWriter();

            writer.write("{\"uuid\":\"" + fileInfo.getUuid() + "\"}");
            writer.flush();

        }catch (Exception e){
            e.printStackTrace();
            request.setAttribute("info", "上传文件失败");
        }

    }

    private String save(Part part, String uuidName,String suffix) throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
        String dir = format.format(new Date());
        File folder = new File(SAVE_PATH + dir);
        if (!folder.exists() && !folder.isDirectory()) {
            // 如果文件夹不存在，创建文件夹
            folder.mkdirs();
        }
        File destFile = new File(folder.getAbsolutePath() + "\\" + uuidName + suffix);
        InputStream is = part.getInputStream();
        OutputStream os = new FileOutputStream(destFile);
        byte[] data = new byte[1024];
        int len;
        while ((len = is.read(data)) > 0) {
            os.write(data, 0, len);
        }
        return destFile.getAbsolutePath();
    }

}
