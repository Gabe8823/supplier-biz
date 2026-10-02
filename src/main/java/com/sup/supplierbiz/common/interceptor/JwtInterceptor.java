package com.sup.supplierbiz.common.interceptor;

import cn.hutool.json.JSONUtil;
import com.sup.supplierbiz.common.enums.ResultCode;
import com.sup.supplierbiz.common.result.Result;
import com.sup.supplierbiz.util.JWTUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {
    private final JWTUtils jwtUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            write401(response);
            return false;
        }
        String token = header.substring(7);

        try {
            Claims claims = jwtUtils.parseToken(token);
            request.setAttribute("userId", claims.getSubject());
            request.setAttribute("username", claims.get("username", String.class));
        } catch (Exception e) {
            write401(response);
            return false;
        }

        return true;
    }
    private void write401(HttpServletResponse response) throws IOException {
        response.setStatus(401);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(
                JSONUtil.toJsonStr(Result.error(ResultCode.UNAUTHORIZED))
        );
    }
}
