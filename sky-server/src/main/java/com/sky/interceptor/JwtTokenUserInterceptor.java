package com.sky.interceptor;

import com.sky.constant.JwtClaimsConstant;
import com.sky.properties.JwtProperties;
import com.sky.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.sky.context.BaseContext;

/**
 * jwt令牌校验的拦截器
 */
@Component
@Slf4j
public class JwtTokenUserInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        
        String requestURI = request.getRequestURI();
        //log.info("====== 开始 JWT 校验 ======");
        //log.info("请求路径：{}", requestURI);

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getUserTokenName());

        try {
            //log.info("jwt 校验中...");
            //log.info("用于解密的 secretKey: {}", jwtProperties.getUserSecretKey());
            //log.info("Token 值：{}", token);
            
            Claims claims = JwtUtil.parseJWT(jwtProperties.getUserSecretKey(), token);
            Long userId = Long.valueOf(claims.get(JwtClaimsConstant.USER_ID).toString());
            BaseContext.setCurrentId(userId);
            
            //log.info("✅ JWT 校验成功！当前用户 id：{}", userId);
            //log.info("====== JWT 校验完成 ======");
            return true;
        } catch (Exception ex) {
           //log.error("❌ JWT 校验失败：{}", ex.getMessage());
           //log.error("错误类型：{}", ex.getClass().getSimpleName());
           //log.error("可能是原因：1.token 过期 2.token 格式错误 3.secretKey 不匹配 4.token 被篡改");
            response.setStatus(401);
            return false;
        }
    }
}
