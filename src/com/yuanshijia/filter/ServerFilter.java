package com.yuanshijia.filter;

import com.yuanshijia.Constants;
import com.yuanshijia.util.RSAUtils;
import com.yuanshijia.util.StringUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuan
 * @date 2019/10/21
 * @description
 */
public class ServerFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        String sid = request.getHeader("X-SID");
        String signature = request.getHeader("X-Signature");


        if (StringUtils.isNotEmpty(sid) && StringUtils.isNotEmpty(signature)) {

            try {
                String decry = RSAUtils.decryptByPublicKey(signature, Constants.PUBLICKEY);
                if (decry.equals(sid)) {
                    // 服务端验证成功
                    filterChain.doFilter(servletRequest, servletResponse);
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        // 不合法，返回403
        response.sendError(HttpServletResponse.SC_FORBIDDEN, "服务端验证失败");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json; charset=utf-8");
    }
}
