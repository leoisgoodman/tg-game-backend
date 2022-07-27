/*
 * tg学习代码
 */
package com.tggame.login.ctrl;

import com.auth0.jwt.JWTCreator;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.tggame.cache.entity.ConstantsEnum;
import com.tggame.cache.entity.RedisKey;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
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
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 玩法
 *
 * @author tg
 */
@RestController
@RequestMapping("/login")
@Slf4j
@Api(value = "玩法控制器", tags = "玩法控制器")
public class LoginController {
    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;


    /**
     * 登录会话
     *
     * @return BetVO
     */
    @ApiOperation(value = "登录会话", notes = "登录会话")
    @GetMapping("/token/{tgGroupId}/{tgUsername}")
    public R token(@PathVariable String tgGroupId, @PathVariable String tgUsername) {
        Group group = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getTgGroupId, tgGroupId)
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
                .withClaim(HttpHeaders.userName, user.getUsername())
                .withClaim(HttpHeaders.tgUsername, user.getTgUsername())
                .withClaim(HttpHeaders.groupId, tgGroupId)
                .withClaim(HttpHeaders.type, user.getType());

        String secret = ConstantsEnum.Jwt_Secret.value;
        String token = JwtToken.createToken(builder, secret, RedisKey.User_Token.time);
        return R.success(token);
    }


}
