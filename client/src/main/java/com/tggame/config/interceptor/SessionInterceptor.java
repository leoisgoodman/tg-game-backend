package com.tggame.config.interceptor;

import com.auth0.jwt.JWTCreator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tggame.cache.entity.ConstantsEnum;
import com.tggame.cache.entity.RedisKey;
import com.tggame.core.base.BaseException;
import com.tggame.core.tools.HttpHeaders;
import com.tggame.core.tools.JwtToken;
import com.tggame.exceptions.GroupException;
import com.tggame.exceptions.UserException;
import com.tggame.group.entity.Group;
import com.tggame.group.entity.GroupStatus;
import com.tggame.group.service.GroupService;
import com.tggame.user.entity.User;
import com.tggame.user.entity.UserStatus;
import com.tggame.user.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 会话拦截器
 */
@Component
@Slf4j
public class SessionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String jwtToken = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
        if (StringUtils.isBlank(jwtToken)) {
            log.error("登录会话不存在token错误");
            throw new BaseException(BaseException.BaseExceptionEnum.Session_Error);
        }

        String userId = null;
        String username = null;
        String tgUsername = null;
        String groupId = null;
        String type = null;
        try {
            userId = JwtToken.getTokenValue(jwtToken, ConstantsEnum.Jwt_Secret.value, HttpHeaders.userId);
            username = JwtToken.getTokenValue(jwtToken, ConstantsEnum.Jwt_Secret.value, HttpHeaders.username);
            tgUsername = JwtToken.getTokenValue(jwtToken, ConstantsEnum.Jwt_Secret.value, HttpHeaders.tgUsername);
            groupId = JwtToken.getTokenValue(jwtToken, ConstantsEnum.Jwt_Secret.value, HttpHeaders.groupId);
            type = JwtToken.getTokenValue(jwtToken, ConstantsEnum.Jwt_Secret.value, HttpHeaders.type);
        } catch (Exception e) {
            log.error("会话异常-{}", e.getLocalizedMessage(), e);
            throw new BaseException(BaseException.BaseExceptionEnum.Session_Error);
        }

        Group group = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getTgGroupId, groupId)
                .eq(Group::getStatus, GroupStatus.Enable));
        if (null == group) {
            throw new GroupException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getGroupId, group.getId())
                .eq(User::getTgUsername, tgUsername)
                .eq(User::getStatus, UserStatus.Enable));
        if (null == user) {
            throw new UserException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }
        JWTCreator.Builder builder = JwtToken.create();
        builder.withClaim(HttpHeaders.userId, user.getId())
                .withClaim(HttpHeaders.username, user.getUsername())
                .withClaim(HttpHeaders.tgUsername, user.getTgUsername())
                .withClaim(HttpHeaders.groupId, groupId)
                .withClaim(HttpHeaders.type, user.getType());

        String secret = ConstantsEnum.Jwt_Secret.value;
        String token = JwtToken.createToken(builder, secret, RedisKey.User_Token.time);
        response.addHeader(org.springframework.http.HttpHeaders.AUTHORIZATION, token);
        request.setAttribute(HttpHeaders.userId, userId);
        request.setAttribute(HttpHeaders.username, username);
        request.setAttribute(HttpHeaders.tgUsername, tgUsername);
        request.setAttribute(HttpHeaders.groupId, groupId);
        request.setAttribute(HttpHeaders.type, type);

        return true;
    }

}
