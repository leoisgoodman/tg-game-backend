/*
 * tg学习代码
 */
package com.tggame.bet.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.bet.entity.BetOrder;
import com.tggame.bet.service.BetOrderService;
import com.tggame.bet.vo.BetOrderPageVO;
import com.tggame.bet.vo.BetOrderSaveVO;
import com.tggame.bet.vo.BetOrderVO;
import com.tggame.bot.entity.Bot;
import com.tggame.bot.service.BotService;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.core.tools.HttpHeaders;
import com.tggame.exceptions.BetOrderException;
import com.tggame.group.entity.Group;
import com.tggame.group.service.GroupService;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.service.OpenRecordService;
import com.tggame.tg.entity.TgGroupBot;
import com.tggame.tg.service.TgGroupBotService;
import com.tggame.trend.entity.TrendRecord;
import com.tggame.trend.service.TrendRecordService;
import com.tggame.user.entity.User;
import com.tggame.user.service.UserService;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 投注
 *
 * @author tg
 */
@RestController
@RequestMapping("/betOrder")
@Slf4j
@Api(value = "投注控制器", tags = "投注控制器")
public class BetOrderController {
    @Autowired
    private BetOrderService betOrderService;

    @Autowired
    private OpenRecordService openRecordService;

    @Autowired
    private TrendRecordService trendRecordService;

    @Autowired
    private UserService userService;

    @Autowired
    private GroupService groupService;

    @Autowired
    private TgGroupBotService tgGroupBotService;

    @Autowired
    private BotService botService;


    /**
     * 创建 投注
     *
     * @return R
     */
    @ApiOperation(value = "创建BetOrder", notes = "创建BetOrder")
    @PostMapping("/build")
    public BetOrderSaveVO build(@ApiParam(name = "创建BetOrder", value = "传入json格式", required = true)
                                @RequestBody BetOrderSaveVO betOrderSaveVO,
                                @RequestAttribute(HttpHeaders.userId) String userId) {

        User user = userService.getById(userId);
        Group group = groupService.getById(user.getGroupId());
        betOrderSaveVO.setTgGroupId(group.getTgGroupId());
        TgGroupBot tgGroupBot = tgGroupBotService.getOne(new QueryWrapper<TgGroupBot>().lambda().eq(TgGroupBot::getGroupId,
                group.getId()));
        Bot bot = botService.getById(tgGroupBot.getBotId());
        betOrderSaveVO.setTgBotId(bot.getTgBotId());
        betOrderSaveVO.setTgUserId(user.getTgUserId());
        BetOrder newBetOrder = new BetOrder();
        BeanUtils.copyProperties(betOrderSaveVO, newBetOrder);

        betOrderService.bet(betOrderSaveVO);

        betOrderSaveVO = new BetOrderSaveVO();
        BeanUtils.copyProperties(newBetOrder, betOrderSaveVO);
        log.debug(JSON.toJSONString(betOrderSaveVO));
        return betOrderSaveVO;
    }


    /**
     * 根据条件tgUserId查询投注一个详情信息
     *
     * @param tgUserId tg下用户id
     * @return BetOrderVO
     */
    @ApiOperation(value = "创建BetOrder", notes = "创建BetOrder")
    @GetMapping("/load/tgUserId/{tgUserId}")
    public BetOrderVO loadByTgUserId(@PathVariable java.lang.String tgUserId) {
        BetOrder betOrder = betOrderService.getOne(new LambdaQueryWrapper<BetOrder>()
                .eq(BetOrder::getTgUserId, tgUserId));
        BetOrderVO betOrderVO = new BetOrderVO();
        BeanUtils.copyProperties(betOrder, betOrderVO);
        log.debug(JSON.toJSONString(betOrderVO));
        return betOrderVO;
    }

    /**
     * 查询投注信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询BetOrder信息集合", notes = "查询BetOrder信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
            @ApiImplicitParam(name = "createTimeBegin", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeEnd", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeBegin", value = "更新时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeEnd", value = "更新时间", paramType = "query")
    })
    @GetMapping(value = "/list")
    public IPage<BetOrderPageVO> list(@ApiIgnore BetOrderPageVO betOrderVO, Integer curPage, Integer pageSize,@RequestAttribute(HttpHeaders.userId) String userId) {
        IPage<BetOrder> page = new Page<>(curPage, pageSize);
        QueryWrapper<BetOrder> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().eq(BetOrder::getUserId,userId);
        if (StringUtils.isNotBlank(betOrderVO.getGroupId())) {
            queryWrapper.lambda().eq(BetOrder::getGroupId, betOrderVO.getGroupId());
        }
        if (StringUtils.isNotBlank(betOrderVO.getTgGroupId())) {
            queryWrapper.lambda().eq(BetOrder::getTgGroupId, betOrderVO.getTgGroupId());
        }
        if (StringUtils.isNotBlank(betOrderVO.getUserId())) {
            queryWrapper.lambda().eq(BetOrder::getUserId, betOrderVO.getUserId());
        }
        if (StringUtils.isNotBlank(betOrderVO.getTgUserId())) {
            queryWrapper.lambda().eq(BetOrder::getTgUserId, betOrderVO.getTgUserId());
        }
        if (StringUtils.isNotBlank(betOrderVO.getBotId())) {
            queryWrapper.lambda().eq(BetOrder::getBotId, betOrderVO.getBotId());
        }
        if (StringUtils.isNotBlank(betOrderVO.getLotteryId())) {
            queryWrapper.lambda().eq(BetOrder::getLotteryId, betOrderVO.getLotteryId());
        }
        if (StringUtils.isNotBlank(betOrderVO.getBetId())) {
            queryWrapper.lambda().eq(BetOrder::getBetId, betOrderVO.getBetId());
        }
        if (StringUtils.isNotBlank(betOrderVO.getBetType())) {
            queryWrapper.lambda().eq(BetOrder::getBetType, betOrderVO.getBetType());
        }
        if (StringUtils.isNotBlank(betOrderVO.getStatus())) {
            queryWrapper.lambda().eq(BetOrder::getStatus, betOrderVO.getStatus());
        }
        if (StringUtils.isNotBlank(betOrderVO.getOpenId())) {
            queryWrapper.lambda().eq(BetOrder::getOpenId, betOrderVO.getOpenId());
        }
        if (betOrderVO.getCreateTimeBegin() != null) {
            queryWrapper.lambda().gt(BetOrder::getCreateTime, betOrderVO.getCreateTimeBegin());
        }
        if (betOrderVO.getCreateTimeEnd() != null) {
            queryWrapper.lambda().lt(BetOrder::getCreateTime, betOrderVO.getCreateTimeEnd());
        }
        if (betOrderVO.getUpdateTimeBegin() != null) {
            queryWrapper.lambda().gt(BetOrder::getUpdateTime, betOrderVO.getUpdateTimeBegin());
        }
        if (betOrderVO.getUpdateTimeEnd() != null) {
            queryWrapper.lambda().lt(BetOrder::getUpdateTime, betOrderVO.getUpdateTimeEnd());
        }
        int total = betOrderService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(BetOrder::getId);
            IPage<BetOrder> betOrderPage = betOrderService.page(page, queryWrapper);
            List<BetOrderPageVO> betOrderPageVOList = JSON.parseArray(JSON.toJSONString(betOrderPage.getRecords()), BetOrderPageVO.class);
            List<String> openIds=
                  betOrderPageVOList.stream().map(BetOrderPageVO::getOpenId).distinct().collect(Collectors.toList());
            List<Long> issueIds =
                   betOrderPageVOList.stream().map(BetOrderPageVO::getIssue).distinct().collect(Collectors.toList());
            List<OpenRecord> openRecordList =
                    openRecordService.list(new QueryWrapper<OpenRecord>().lambda().in(OpenRecord::getId,openIds));
            List<TrendRecord> trendRecordList =
                  trendRecordService.list(new QueryWrapper<TrendRecord>().lambda().in(TrendRecord::getIssue,issueIds));
            for(BetOrderPageVO betOrderPageVO : betOrderPageVOList){
                for(OpenRecord openRecord : openRecordList ){
                  if(betOrderPageVO.getOpenId().equals(openRecord.getId())){
                    betOrderPageVO.setBtcValue(openRecord.getNum());
                  }
                }
                for(TrendRecord trendRecord : trendRecordList){
                  if(betOrderPageVO.getIssue().equals(trendRecord.getIssue())){
                    betOrderPageVO.setOpenData(trendRecord.getData());
                  }
                }
            }
            IPage<BetOrderPageVO> iPage = new Page<>();
            iPage.setPages(betOrderPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(betOrderPage.getTotal());
            iPage.setRecords(betOrderPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 投注
     *
     * @return R
     */
    @ApiOperation(value = "修改BetOrder", notes = "修改BetOrder")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改BetOrder", value = "传入json格式", required = true)
                          @RequestBody BetOrderVO betOrderVO) {
        if (StringUtils.isBlank(betOrderVO.getId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        BetOrder newBetOrder = new BetOrder();
        BeanUtils.copyProperties(betOrderVO, newBetOrder);
        boolean isUpdated = betOrderService.update(newBetOrder, new LambdaQueryWrapper<BetOrder>()
                .eq(BetOrder::getId, betOrderVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 投注
     *
     * @return R
     */
    @ApiOperation(value = "删除BetOrder", notes = "删除BetOrder")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
            @ApiImplicitParam(name = "tgUserId", value = "tg下用户id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore BetOrderVO betOrderVO) {
        if (StringUtils.isBlank(betOrderVO.getId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        BetOrder newBetOrder = new BetOrder();
        BeanUtils.copyProperties(betOrderVO, newBetOrder);
        betOrderService.remove(new LambdaQueryWrapper<BetOrder>()
                .eq(BetOrder::getId, betOrderVO.getId()));
        return R.success("删除成功");
    }

}
