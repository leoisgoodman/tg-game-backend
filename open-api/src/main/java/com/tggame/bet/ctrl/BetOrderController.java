/*
 * tg学习代码
 */
package com.tggame.bet.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.bet.entity.BetCode;
import com.tggame.bet.entity.BetOrder;
import com.tggame.bet.entity.BetOrderStatus;
import com.tggame.bet.entity.BetType;
import com.tggame.bet.service.BetOrderService;
import com.tggame.bet.vo.BetOrderPageVO;
import com.tggame.bet.vo.BetOrderSaveVO;
import com.tggame.bet.vo.BetOrderVO;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.exceptions.BetOrderException;
import com.tggame.exceptions.OpenRecordException;
import com.tggame.open.entity.OpenEnum;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.entity.OpenRecordStatus;
import com.tggame.open.service.OpenRecordService;
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
    private UserService userService;

    @Autowired
    private OpenRecordService openRecordService;


    /**
     * 创建 投注
     * todo 实现投注订单，需要判断是否有余额，以及是否停止当前期是否停止投注
     *
     * @return R
     */
    @ApiOperation(value = "创建BetOrder", notes = "创建BetOrder")
    @PostMapping("/build")
    public BetOrderSaveVO build(@ApiParam(name = "创建BetOrder", value = "传入json格式", required = true)
                                @RequestBody BetOrderSaveVO betOrderSaveVO) {

        if (StringUtils.isBlank(betOrderSaveVO.getTgGroupId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(betOrderSaveVO.getTgUserId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        BetOrder newBetOrder = new BetOrder();
        BeanUtils.copyProperties(betOrderSaveVO, newBetOrder);

        betOrderService.bet(newBetOrder);

        betOrderSaveVO = new BetOrderSaveVO();
        BeanUtils.copyProperties(newBetOrder, betOrderSaveVO);
        log.debug(JSON.toJSONString(betOrderSaveVO));
        return betOrderSaveVO;
    }


    /**
     * 根据条件tgUserId查询投注一个详情信息
     *
     * @return BetOrderVO
     */
    @ApiOperation(value = "根据条件tgUserId查询投注一个详情信息", notes = "根据条件tgUserId查询投注一个详情信息")
    @GetMapping("/load/{tgGroupId}/{tgUserId}/{tgBotId}")
    public R loadDetail(@PathVariable String tgGroupId, @PathVariable String tgUserId, @PathVariable String tgBotId) {
        OpenRecord openRecord = openRecordService.getOne(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getStatus, OpenRecordStatus.Enable));
        if (null == openRecord) {
            log.error("查询期号不存在");
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }

        List<BetOrder> betOrderList = betOrderService.list(new LambdaQueryWrapper<BetOrder>()
                .eq(BetOrder::getTgGroupId, tgGroupId)
                .eq(BetOrder::getTgUserId, tgUserId)
                .eq(BetOrder::getOpenId, openRecord.getId())
                .eq(BetOrder::getIssue, openRecord.getIssue()));

        if (CollectionUtils.isNotEmpty(betOrderList)) {
            List<String> betNumList = betOrderList.stream()
                    .filter(betOrder -> BetType.Num == BetType.getEnum(betOrder.getBetType()))
                    .map(betOrder -> betOrder.getBetNum())
                    .collect(Collectors.toList());

            int bigCount = betOrderList.stream()
                    .filter(betOrder -> BetCode.Big == BetCode.getEnum(betOrder.getBetCode()))
                    .collect(Collectors.toList()).size();
            int smallCount = betOrderList.stream()
                    .filter(betOrder -> BetCode.Small == BetCode.getEnum(betOrder.getBetCode()))
                    .collect(Collectors.toList()).size();
            int oddCount = betOrderList.stream()
                    .filter(betOrder -> BetCode.Odd == BetCode.getEnum(betOrder.getBetCode()))
                    .collect(Collectors.toList()).size();
            int evenCount = betOrderList.stream()
                    .filter(betOrder -> BetCode.Even == BetCode.getEnum(betOrder.getBetCode()))
                    .collect(Collectors.toList()).size();

            String num = CollectionUtils.isEmpty(betNumList) ? "-" : StringUtils.join(betNumList, ",");

            User user = userService.getOne(new LambdaQueryWrapper<User>()
                    .eq(User::getTgUserId, tgUserId));

            String detail = "第%s期合计投注:\n" +
                    "大:%s|小:%s|单:%s|双:%s\n" +
                    "号码:%s\n" +
                    "@%s\n";
            detail = String.format(detail, openRecord.getIssue(), bigCount, smallCount, oddCount, evenCount, num, user.getTgUsername());

            return R.success(detail);
        }

        return R.success("您暂无投注");
    }


    /**
     * 根据群id，统计当期投注情况
     *
     * @return BetOrderVO
     */
    @ApiOperation(value = "根据群id，统计当期投注情况", notes = "根据群id，统计当期投注情况")
    @GetMapping("/count/{tgGroupId}")
    public R countDetail(@PathVariable String tgGroupId) {
        OpenRecord openRecord = openRecordService.getOne(new LambdaQueryWrapper<OpenRecord>()
                .in(OpenRecord::getStatus, OpenRecordStatus.Lock, OpenRecordStatus.Drawn)
                .orderByDesc(OpenRecord::getId)
                .last("limit 1"));
        if (null == openRecord) {
            log.error("查询期号不存在");
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Result_Not_Exist);
        }
        if (null == openRecord.getNum()) {
            log.warn("尚未開獎，稍等再試-{}", openRecord);
            throw new BetOrderException(BaseException.BaseExceptionEnum.Not_Drawn);
        }

        OpenRecord nextOpenRecord = openRecordService.getOne(new LambdaQueryWrapper<OpenRecord>()
                .in(OpenRecord::getStatus, OpenRecordStatus.Enable)
                .orderByDesc(OpenRecord::getId)
                .last("limit 1"));


        List<BetOrder> betOrderList = betOrderService.list(new LambdaQueryWrapper<BetOrder>()
                .eq(BetOrder::getTgGroupId, tgGroupId)
                .eq(BetOrder::getIssue, openRecord.getIssue()));

        String profitLoss = "无人投注";
        if (CollectionUtils.isNotEmpty(betOrderList)) {
            List<BetOrder> betOrderFilterList = betOrderList.stream().filter(betOrder -> BetOrderStatus.Sent_Money_Done == BetOrderStatus.getEnum(betOrder.getStatus())
                    || BetOrderStatus.Win == BetOrderStatus.getEnum(betOrder.getStatus()))
                    .collect(Collectors.toList());
            if (CollectionUtils.isNotEmpty(betOrderFilterList)) {
                List<User> userList = userService.list(new LambdaQueryWrapper<User>()
                        .select(User::getId, User::getTgUsername)
                        .in(User::getId, betOrderFilterList.stream().map(betOrder -> betOrder.getUserId()).collect(Collectors.toSet())));
                profitLoss = "";
                for (BetOrder betOrder : betOrderFilterList) {
                    for (User user : userList) {
                        if (user.getId().equals(betOrder.getUserId())) {
                            profitLoss += "@" + user.getTgUsername() + " 盈 " + betOrder.getShouldPayAmount() + " \n";
                        }
                    }
                }
            } else {
                profitLoss = "无人中奖";
            }

        }

        String[] resultArray = OpenEnum.Instance.drawn(openRecord.getNum());
        String result = String.join(",", resultArray);
        //todo 投注统计以上数据进行替换，下方提炼成模板模式
        String detail = "第%s期\n" +
                "BTC/USDT: %s\n" +
                "%s\n" +
                "盈亏统计：\n" +
                "%s\n" +
                "\n" +
                "第%s期\n" +
                "开奖时间：%s\n" +
                "投注中...\n" +
                "本期枱红 10000 试玩\n" +
                "两面赔率 1.96\n" +
                "大小限红 10 ~ 1000\n" +
                "单双限红 10 ~ 1000\n" +
                "号码赔率 9.8\n" +
                "号码限红 10 ~ 500";
        detail = String.format(detail, openRecord.getIssue(), openRecord.getNum(), result, profitLoss, nextOpenRecord.getIssue(), nextOpenRecord.getOpenTime());
        return R.success(detail);
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
    public IPage<BetOrderPageVO> list(@ApiIgnore BetOrderPageVO betOrderVO, Integer curPage, Integer pageSize) {
        IPage<BetOrder> page = new Page<>(curPage, pageSize);
        QueryWrapper<BetOrder> queryWrapper = new QueryWrapper<>();
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
