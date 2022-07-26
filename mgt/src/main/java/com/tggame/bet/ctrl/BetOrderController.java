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
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.exceptions.BetOrderException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

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


    /**
     * 创建 投注
     *
     * @return R
     */
    @ApiOperation(value = "创建BetOrder", notes = "创建BetOrder")
    @PostMapping("/build")
    public BetOrderSaveVO build(@ApiParam(name = "创建BetOrder", value = "传入json格式", required = true)
                                @RequestBody BetOrderSaveVO betOrderSaveVO) {
        if (StringUtils.isBlank(betOrderSaveVO.getId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getGroupId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getTgGroupId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getUserId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getTgUserId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getBotId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getLotteryId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getLotteryName())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getBetId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getBetName())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getBetNum())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(betOrderSaveVO.getBetType())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(betOrderSaveVO.getStatus())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(betOrderSaveVO.getOpenId())) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Empty_Param);
        }


        int count = betOrderService.count(new LambdaQueryWrapper<BetOrder>()
                .eq(BetOrder::getId, betOrderSaveVO.getId())
                .eq(BetOrder::getGroupId, betOrderSaveVO.getGroupId())
                .eq(BetOrder::getTgGroupId, betOrderSaveVO.getTgGroupId())
                .eq(BetOrder::getUserId, betOrderSaveVO.getUserId())
                .eq(BetOrder::getTgUserId, betOrderSaveVO.getTgUserId())
                .eq(BetOrder::getBotId, betOrderSaveVO.getBotId())
                .eq(BetOrder::getLotteryId, betOrderSaveVO.getLotteryId())
                .eq(BetOrder::getLotteryName, betOrderSaveVO.getLotteryName())
                .eq(BetOrder::getBetId, betOrderSaveVO.getBetId())
                .eq(BetOrder::getBetName, betOrderSaveVO.getBetName())
                .eq(BetOrder::getBetNum, betOrderSaveVO.getBetNum())
                .eq(BetOrder::getOdds, betOrderSaveVO.getOdds())
                .eq(BetOrder::getBetType, betOrderSaveVO.getBetType())
                .eq(BetOrder::getPayBackPercent, betOrderSaveVO.getPayBackPercent())
                .eq(BetOrder::getAmount, betOrderSaveVO.getAmount())
                .eq(BetOrder::getWinAmount, betOrderSaveVO.getWinAmount())
                .eq(BetOrder::getShouldPayAmount, betOrderSaveVO.getShouldPayAmount())
                .eq(BetOrder::getStatus, betOrderSaveVO.getStatus())
                .eq(BetOrder::getOpenId, betOrderSaveVO.getOpenId())
                .eq(BetOrder::getCreateTime, betOrderSaveVO.getCreateTime())
                .eq(BetOrder::getUpdateTime, betOrderSaveVO.getUpdateTime())
        );
        if (count > 0) {
            throw new BetOrderException(BaseException.BaseExceptionEnum.Exists);
        }

        BetOrder newBetOrder = new BetOrder();
        BeanUtils.copyProperties(betOrderSaveVO, newBetOrder);

        betOrderService.save(newBetOrder);

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
