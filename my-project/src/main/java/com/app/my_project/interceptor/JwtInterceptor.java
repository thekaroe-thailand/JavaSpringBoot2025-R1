package com.app.my_project.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.app.my_project.WebConfig;
import com.app.my_project.annotation.RequireAuth;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;

@Component
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            HttpServletRequest request,
            HttpServletResponse response,
            Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            return true;
        }

        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        boolean hasRequireAuth = handlerMethod.hasMethodAnnotation(RequireAuth.class);

        if (!hasRequireAuth) {
            return true;
        }

        String token = request.getHeader("Authorization");

        if (token == null || !token.startsWith("Bearer ")) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

        try {
            String tokenWithoutBearer = token.replace("Bearer ", "");
            JWT.require(Algorithm.HMAC256(WebConfig.getSecret()))
                    .build()
                    .verify(tokenWithoutBearer);

            return true;
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return false;
        }

    }
}
