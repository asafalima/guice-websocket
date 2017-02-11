package com.asafalima.websocket.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Asaf Alima
 */
public class AuthenticationFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("Initializing filter, config: {}", filterConfig);
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        LOGGER.info("Checking client, request: {}", request);

        if ((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse)) {
            HttpServletRequest httpServletRequest = (HttpServletRequest) request;
            HttpServletResponse httpServletResponse = (HttpServletResponse) response;
            Cookie[] cookies = httpServletRequest.getCookies();
            for (Cookie cookie : cookies) {
                //if (cookie.getName().equals("accessToken") && cookie.getValue().equals("top-secret-token")) {
                if (cookie.getName().equals("__utma") && cookie.getValue().equals("111872281.1941175145.1475325529.1475538295.1477149284.3")) {
                    LOGGER.info("Finish checking client - client is authenticated");
                    chain.doFilter(request, response);
                    return;
                }
            }


            LOGGER.warn("Finish checking client - client is unauthenticated !");
            httpServletResponse.setStatus(401);
            httpServletResponse.getWriter().print("Invalid token !");
        }
    }

    @Override
    public void destroy() {

    }

}
