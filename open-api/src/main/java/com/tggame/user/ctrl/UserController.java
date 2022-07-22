/*
 * tg学习代码
 */
package com.tggame.user.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.exceptions.UserException;
import com.tggame.user.entity.User;
import com.tggame.user.service.UserService;
import com.tggame.user.vo.UserPageVO;
import com.tggame.user.vo.UserSaveVO;
import com.tggame.user.vo.UserVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 群成员
 *
 * @author tg
 */
@RestController
@RequestMapping("/user")
@Slf4j
@Api(value = "群成员控制器", tags = "群成员控制器")
public class UserController {
    @Autowired
    private UserService userService;


    /**
     * 创建 群成员
     * 加群时自动处理
     *
     * @return R
     */
    @ApiOperation(value = "创建User", notes = "创建User")
    @PostMapping("/build")
    public String build(@ApiParam(name = "创建User", value = "传入json格式", required = true)
                        @RequestBody UserSaveVO userSaveVO) {

        if (StringUtils.isBlank(userSaveVO.getGroupId())) {
            throw new UserException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(userSaveVO.getTgUserId())) {
            throw new UserException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(userSaveVO.getTgUsername())) {
            throw new UserException(BaseException.BaseExceptionEnum.Empty_Param);
        }


        int count = userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getGroupId, userSaveVO.getGroupId())
                .eq(User::getTgUserId, userSaveVO.getTgUserId()));
        if (count > 0) {
            throw new UserException(BaseException.BaseExceptionEnum.Exists);
        }

        User newUser = new User();
        BeanUtils.copyProperties(userSaveVO, newUser);
        //todo 需要处理创建用户以及加群操作
        userService.save(newUser);

        String toNewUser = "欢迎 @{at} 加入\n" +
                "快速开始教程\n" +
                "1. 每2分钟开奖一次, 开奖前20秒封盘，停止投注\n" +
                "2. 开奖号取币安的BTC/USDT价格双数分钟的开盘价，取值到小数点后一位，例如币安BTC/USDT的价格是49617.8，则开奖号码的算法：4+9+6+1+7+8=35，取个位数5为开奖号码\n" +
                "3. 本系统专业为博彩双方服务，不参与任何形式博彩，无需担心庄家输了赖帐，请放心存款\n" +
                "4. 操作命令：\n" +
                "/start 开启彩票\n" +
                "/stop 关闭彩票\n" +
                "/mini H5小程序 \n" +
                "/help 查看帮助\n" +
                "/doc 操作指南";

        return toNewUser;
    }


    /**
     * 根据条件tgUserId查询群成员一个详情信息
     *
     * @param tgUserId tg用户id，来自tg
     * @return UserVO
     */
    @ApiOperation(value = "创建User", notes = "创建User")
    @GetMapping("/load/tgUserId/{tgUserId}")
    public UserVO loadByTgUserId(@PathVariable java.lang.String tgUserId) {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getTgUserId, tgUserId));
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        log.debug(JSON.toJSONString(userVO));
        return userVO;
    }

    /**
     * 根据条件tgUsername查询群成员一个详情信息
     *
     * @param tgUsername tg下用户名
     * @return UserVO
     */
    @ApiOperation(value = "创建User", notes = "创建User")
    @GetMapping("/load/tgUsername/{tgUsername}")
    public UserVO loadByTgUsername(@PathVariable java.lang.String tgUsername) {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getTgUsername, tgUsername));
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        log.debug(JSON.toJSONString(userVO));
        return userVO;
    }

    /**
     * 根据条件username查询群成员一个详情信息
     *
     * @param username 本系统生成的用户名，默认和tg保持一致
     * @return UserVO
     */
    @ApiOperation(value = "创建User", notes = "创建User")
    @GetMapping("/load/username/{username}")
    public UserVO loadByUsername(@PathVariable java.lang.String username) {
        User user = userService.getOne(new LambdaQueryWrapper<User>()
                .eq(User::getUsername, username));
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        log.debug(JSON.toJSONString(userVO));
        return userVO;
    }

    /**
     * 查询群成员信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询User信息集合", notes = "查询User信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
            @ApiImplicitParam(name = "createTimeBegin", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeEnd", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeBegin", value = "更新时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeEnd", value = "更新时间", paramType = "query")
    })
    @GetMapping(value = "/list")
    public IPage<UserPageVO> list(@ApiIgnore UserPageVO userVO, Integer curPage, Integer pageSize) {
        IPage<User> page = new Page<>(curPage, pageSize);
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(userVO.getGroupId())) {
            queryWrapper.lambda().eq(User::getGroupId, userVO.getGroupId());
        }
        if (StringUtils.isNotBlank(userVO.getTgUserId())) {
            queryWrapper.lambda().eq(User::getTgUserId, userVO.getTgUserId());
        }
        if (StringUtils.isNotBlank(userVO.getTgUsername())) {
            queryWrapper.lambda().eq(User::getTgUsername, userVO.getTgUsername());
        }
        if (StringUtils.isNotBlank(userVO.getUsername())) {
            queryWrapper.lambda().eq(User::getUsername, userVO.getUsername());
        }
        if (StringUtils.isNotBlank(userVO.getStatus())) {
            queryWrapper.lambda().eq(User::getStatus, userVO.getStatus());
        }
        if (StringUtils.isNotBlank(userVO.getType())) {
            queryWrapper.lambda().eq(User::getType, userVO.getType());
        }
        if (StringUtils.isNotBlank(userVO.getIsOwner())) {
            queryWrapper.lambda().eq(User::getIsOwner, userVO.getIsOwner());
        }
        if (StringUtils.isNotBlank(userVO.getIsAdmin())) {
            queryWrapper.lambda().eq(User::getIsAdmin, userVO.getIsAdmin());
        }

        if (StringUtils.isNotBlank(userVO.getUsdtAddress())) {
            queryWrapper.lambda().eq(User::getUsdtAddress, userVO.getUsdtAddress());
        }
        if (userVO.getCreateTimeBegin() != null) {
            queryWrapper.lambda().gt(User::getCreateTime, userVO.getCreateTimeBegin());
        }
        if (userVO.getCreateTimeEnd() != null) {
            queryWrapper.lambda().lt(User::getCreateTime, userVO.getCreateTimeEnd());
        }
        if (userVO.getUpdateTimeBegin() != null) {
            queryWrapper.lambda().gt(User::getUpdateTime, userVO.getUpdateTimeBegin());
        }
        if (userVO.getUpdateTimeEnd() != null) {
            queryWrapper.lambda().lt(User::getUpdateTime, userVO.getUpdateTimeEnd());
        }
        int total = userService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(User::getId);

            IPage<User> userPage = userService.page(page, queryWrapper);
            List<UserPageVO> userPageVOList = JSON.parseArray(JSON.toJSONString(userPage.getRecords()), UserPageVO.class);
            IPage<UserPageVO> iPage = new Page<>();
            iPage.setPages(userPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(userPage.getTotal());
            iPage.setRecords(userPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 群成员
     *
     * @return R
     */
    @ApiOperation(value = "修改User", notes = "修改User")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改User", value = "传入json格式", required = true)
                          @RequestBody UserVO userVO) {
        if (StringUtils.isBlank(userVO.getId())) {
            throw new UserException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        User newUser = new User();
        BeanUtils.copyProperties(userVO, newUser);
        boolean isUpdated = userService.update(newUser, new LambdaQueryWrapper<User>()
                .eq(User::getId, userVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 群成员
     *
     * @return R
     */
    @ApiOperation(value = "删除User", notes = "删除User")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
            @ApiImplicitParam(name = "tgUserId", value = "tg用户id，来自tg", paramType = "query"),
            @ApiImplicitParam(name = "tgUsername", value = "tg下用户名", paramType = "query"),
            @ApiImplicitParam(name = "username", value = "本系统生成的用户名，默认和tg保持一致", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore UserVO userVO) {
        if (StringUtils.isBlank(userVO.getId())) {
            throw new UserException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        User newUser = new User();
        BeanUtils.copyProperties(userVO, newUser);
        userService.remove(new LambdaQueryWrapper<User>()
                .eq(User::getId, userVO.getId()));
        return R.success("删除成功");
    }

}
