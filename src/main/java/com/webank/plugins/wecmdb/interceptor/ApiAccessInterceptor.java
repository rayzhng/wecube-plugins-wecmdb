package com.webank.plugins.wecmdb.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiAccessInterceptor implements HandlerInterceptor {
    private static final String DEFAULT_SUPER_ADMIN = "SUPER_ADMIN";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String roleName = request.getHeader("roleName");
        if (roleName != null) {
            response.setHeader("roleName", roleName);
            RoleStorage.getIntance().set(roleName);
        } else {
            //TO DO: Hard code for testing only, will remove later on
            //throw new PluginException("Required parameter 'roleName' is missing in header.");
            response.setHeader("roleName", DEFAULT_SUPER_ADMIN);
            RoleStorage.getIntance().set(DEFAULT_SUPER_ADMIN);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        RoleStorage.getIntance().remove();
    }
}
