/*
 * tg学习代码
 */
package com.tggame.bet.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.bet.entity.Bet;
import com.tggame.bet.service.BetService;
import com.tggame.bet.vo.BetPageVO;
import com.tggame.bet.vo.BetSaveVO;
import com.tggame.bet.vo.BetVO;
import com.tggame.cache.entity.RedisKey;
import com.tggame.cache.service.RedisServiceSVImpl;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.exceptions.BetException;
import com.tggame.exceptions.GroupException;
import com.tggame.group.entity.Group;
import com.tggame.group.service.GroupService;
import com.tggame.user.entity.User;
import com.tggame.user.entity.UserType;
import com.tggame.user.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 玩法
 *
 * @author tg
 */
@RestController
@RequestMapping("/bet")
@Slf4j
@Api(value = "玩法控制器", tags = "玩法控制器")
public class BetController {
    @Autowired
    private BetService betService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private RedisServiceSVImpl redisServiceSV;


    /**
     * 创建 玩法
     *
     * @return R
     */
    @ApiOperation(value = "创建Bet", notes = "创建Bet")
    @PostMapping("/build")
    public BetSaveVO build(@ApiParam(name = "创建Bet", value = "传入json格式", required = true)
                           @RequestBody BetSaveVO betSaveVO) {
        if (StringUtils.isBlank(betSaveVO.getId())) {
            throw new BetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betSaveVO.getLotteryId())) {
            throw new BetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betSaveVO.getName())) {
            throw new BetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betSaveVO.getCode())) {
            throw new BetException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(betSaveVO.getStatus())) {
            throw new BetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betSaveVO.getSummary())) {
            throw new BetException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        int count = betService.count(new LambdaQueryWrapper<Bet>()
                .eq(Bet::getId, betSaveVO.getId())
                .eq(Bet::getLotteryId, betSaveVO.getLotteryId())
                .eq(Bet::getName, betSaveVO.getName())
                .eq(Bet::getCode, betSaveVO.getCode())
                .eq(Bet::getMinBetMoney, betSaveVO.getMinBetMoney())
                .eq(Bet::getMaxBetMoney, betSaveVO.getMaxBetMoney())
                .eq(Bet::getOdds, betSaveVO.getOdds())
                .eq(Bet::getDefaultMoney, betSaveVO.getDefaultMoney())
                .eq(Bet::getType, betSaveVO.getType())
                .eq(Bet::getPayBackPercent, betSaveVO.getPayBackPercent())
                .eq(Bet::getStatus, betSaveVO.getStatus())
                .eq(Bet::getSummary, betSaveVO.getSummary())
        );
        if (count > 0) {
            throw new BetException(BaseException.BaseExceptionEnum.Exists);
        }

        Bet newBet = new Bet();
        BeanUtils.copyProperties(betSaveVO, newBet);

        betService.save(newBet);

        betSaveVO = new BetSaveVO();
        BeanUtils.copyProperties(newBet, betSaveVO);
        log.debug(JSON.toJSONString(betSaveVO));
        return betSaveVO;
    }


    /**
     * 根据条件code查询玩法一个详情信息
     *
     * @param code 编码
     * @return BetVO
     */
    @ApiOperation(value = "创建Bet", notes = "创建Bet")
    @GetMapping("/load/code/{code}")
    public BetVO loadByCode(@PathVariable java.lang.String code) {
        Bet bet = betService.getOne(new LambdaQueryWrapper<Bet>()
                .eq(Bet::getCode, code));
        BetVO betVO = new BetVO();
        BeanUtils.copyProperties(bet, betVO);
        log.debug(JSON.toJSONString(betVO));
        return betVO;
    }

    /**
     * 查询玩法信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询Bet信息集合", notes = "查询Bet信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
    })
    @GetMapping(value = "/list")
    public IPage<BetPageVO> list(@ApiIgnore BetPageVO betVO, Integer curPage, Integer pageSize) {
        IPage<Bet> page = new Page<>(curPage, pageSize);
        QueryWrapper<Bet> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(betVO.getLotteryId())) {
            queryWrapper.lambda().eq(Bet::getLotteryId, betVO.getLotteryId());
        }

        if (StringUtils.isNotBlank(betVO.getType())) {
            queryWrapper.lambda().eq(Bet::getType, betVO.getType());
        }
        if (StringUtils.isNotBlank(betVO.getStatus())) {
            queryWrapper.lambda().eq(Bet::getStatus, betVO.getStatus());
        }
        int total = betService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(Bet::getId);

            IPage<Bet> betPage = betService.page(page, queryWrapper);
            List<BetPageVO> betPageVOList = JSON.parseArray(JSON.toJSONString(betPage.getRecords()), BetPageVO.class);
            IPage<BetPageVO> iPage = new Page<>();
            iPage.setPages(betPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(betPage.getTotal());
            iPage.setRecords(betPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 开始投注控制
     *
     * @return R
     */
    @ApiOperation(value = "开始投注控制", notes = "开始投注控制")
    @PutMapping("/start/{tgGroupId}/{tgBotId}/{tgUserId}")
    public boolean start(@PathVariable String tgGroupId, @PathVariable String tgBotId, @PathVariable String tgUserId) {
        Group group = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getTgGroupId, tgGroupId));
        if (null == group) {
            throw new GroupException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        int count = userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getTgUserId, tgUserId)
                .eq(User::getGroupId, group.getId())
                .eq(User::getType, UserType.Banker));
        if (count <= 0) {
            return false;
        }

        String cacheKey = RedisKey.getBetStatusKey(tgGroupId, tgBotId);
        redisServiceSV.set(cacheKey, true);
        return true;
    }

    /**
     * 停止投注控制
     *
     * @return R
     */
    @ApiOperation(value = "停止投注控制", notes = "停止投注控制")
    @PutMapping("/stop/{tgGroupId}/{tgBotId}/{tgUserId}")
    public boolean stop(@PathVariable String tgGroupId, @PathVariable String tgBotId, @PathVariable String tgUserId) {
        Group group = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getTgGroupId, tgGroupId));
        if (null == group) {
            throw new GroupException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        int count = userService.count(new LambdaQueryWrapper<User>()
                .eq(User::getTgUserId, tgUserId)
                .eq(User::getGroupId, group.getId())
                .eq(User::getType, UserType.Banker));
        if (count <= 0) {
            return false;
        }
        String cacheKey = RedisKey.getBetStatusKey(tgGroupId, tgBotId);
        redisServiceSV.set(cacheKey, false);
        return true;
    }

    /**
     * 是否可以投注状态
     *
     * @return R
     */
    @ApiOperation(value = "是否可以投注状态", notes = "是否可以投注状态")
    @GetMapping("/status/{tgGroupId}/{tgBotId}")
    public boolean status(@PathVariable String tgGroupId, @PathVariable String tgBotId) {
        if (StringUtils.isBlank(tgGroupId)) {
            throw new BetException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        if (StringUtils.isBlank(tgBotId)) {
            throw new BetException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        String cacheKey = RedisKey.getBetStatusKey(tgGroupId, tgBotId);
        boolean isBet = (boolean) redisServiceSV.get(cacheKey);
        return isBet;
    }

    /**
     * 修改 玩法
     *
     * @return R
     */
    @ApiOperation(value = "修改Bet", notes = "修改Bet")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改Bet", value = "传入json格式", required = true)
                          @RequestBody BetVO betVO) {
        if (StringUtils.isBlank(betVO.getId())) {
            throw new BetException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Bet newBet = new Bet();
        BeanUtils.copyProperties(betVO, newBet);
        boolean isUpdated = betService.update(newBet, new LambdaQueryWrapper<Bet>()
                .eq(Bet::getId, betVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 玩法
     *
     * @return R
     */
    @ApiOperation(value = "删除Bet", notes = "删除Bet")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "编码", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore BetVO betVO) {
        if (StringUtils.isBlank(betVO.getId())) {
            throw new BetException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Bet newBet = new Bet();
        BeanUtils.copyProperties(betVO, newBet);
        betService.remove(new LambdaQueryWrapper<Bet>()
                .eq(Bet::getId, betVO.getId()));
        return R.success("删除成功");
    }

}
