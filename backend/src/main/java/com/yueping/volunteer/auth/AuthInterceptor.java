package com.yueping.volunteer.auth;

import com.yueping.volunteer.model.UserProfile;
import com.yueping.volunteer.model.UserRole;
import com.yueping.volunteer.repository.UserProfileRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenService jwtTokenService;
    private final UserProfileRepository userProfileRepository;

    public AuthInterceptor(JwtTokenService jwtTokenService, UserProfileRepository userProfileRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userProfileRepository = userProfileRepository;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        AuthRequired authRequired = handlerMethod.getMethodAnnotation(AuthRequired.class);
        if (authRequired == null) {
            authRequired = handlerMethod.getBeanType().getAnnotation(AuthRequired.class);
        }
        if (authRequired == null) {
            return true;
        }

        String authorization = request.getHeader("Authorization");
        if (authorization == null || !authorization.startsWith("Bearer ")) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "未登录或登录已过期");
        }

        try {
            AuthenticatedUser tokenUser = jwtTokenService.parseToken(authorization.substring(7));
            UserProfile currentUser = userProfileRepository.findById(tokenUser.getUserId())
                    .orElseThrow(() -> new AuthException(HttpStatus.UNAUTHORIZED, "当前用户不存在"));

            UserRole role = currentUser.getRole();
            if (role == null && !authRequired.allowUnassigned()) {
                throw new AuthException(HttpStatus.FORBIDDEN, "请先完成身份选择");
            }

            if (authRequired.roles().length > 0) {
                if (role == null || Arrays.stream(authRequired.roles()).noneMatch(role::canAccess)) {
                    throw new AuthException(HttpStatus.FORBIDDEN, "当前账号无权访问");
                }
            }

            AuthContext.set(new AuthenticatedUser(
                    currentUser.getId(),
                    currentUser.getOpenId(),
                    currentUser.getName(),
                    role
            ));
            return true;
        } catch (AuthException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new AuthException(HttpStatus.UNAUTHORIZED, "登录态校验失败");
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthContext.clear();
    }
}
