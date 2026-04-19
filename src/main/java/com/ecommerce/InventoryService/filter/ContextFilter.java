package com.ecommerce.InventoryService.filter;

import com.ecommerce.InventoryService.common.AppConstants;
import com.ecommerce.InventoryService.util.UserContext;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;

        String user = req.getHeader(AppConstants.USER_HEADER);
        String role = req.getHeader(AppConstants.ROLE_HEADER);

        UserContext.set(user, role);

        try {
            chain.doFilter(request, response);
        } finally {
            UserContext.clear();
        }
    }
}