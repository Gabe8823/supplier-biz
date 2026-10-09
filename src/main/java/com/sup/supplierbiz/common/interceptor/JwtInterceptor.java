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

/**
 * JWT 认证拦截器
 *
 * @author sup
 * @date 2026-10-09
 */
@Component
@RequiredArgsConstructor
public class JwtInterceptor implements HandlerInterceptor {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    private static final int TOKEN_START_INDEX = BEARER_PREFIX.length();

    private final JWTUtils jwtUtils;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String header = request.getHeader(AUTHORIZATION_HEADER);
        if (header == null || !header.startsWith(BEARER_PREFIX)) {
            write401(response);
            return false;
        }
        String token = header.substring(TOKEN_START_INDEX);

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
