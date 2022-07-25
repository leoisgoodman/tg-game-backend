/*
 * tg学习代码
 */
package com.tggame.trend.ctrl;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.collect.Lists;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.exceptions.TrendRecordException;
import com.tggame.trend.entity.TrendRecord;
import com.tggame.trend.service.TrendRecordService;
import com.tggame.trend.vo.TrendRecordPageVO;
import com.tggame.trend.vo.TrendRecordSaveVO;
import com.tggame.trend.vo.TrendRecordVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 走势记录
 *
 * @author tg
 */
@RestController
@RequestMapping("/trendRecord")
@Slf4j
@Api(value = "走势记录控制器", tags = "走势记录控制器")
public class TrendRecordController {
    @Autowired
    private TrendRecordService trendRecordService;


    /**
     * 创建 走势记录
     *
     * @return R
     */
    @ApiOperation(value = "创建TrendRecord", notes = "创建TrendRecord")
    @PostMapping("/build")
    public TrendRecordSaveVO build(@ApiParam(name = "创建TrendRecord", value = "传入json格式", required = true)
                                   @RequestBody TrendRecordSaveVO trendRecordSaveVO) {
        if (StringUtils.isBlank(trendRecordSaveVO.getId())) {
            throw new TrendRecordException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(trendRecordSaveVO.getLotteryId())) {
            throw new TrendRecordException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(trendRecordSaveVO.getData())) {
            throw new TrendRecordException(BaseException.BaseExceptionEnum.Empty_Param);
        }


        int count = trendRecordService.count(new LambdaQueryWrapper<TrendRecord>()
                .eq(TrendRecord::getId, trendRecordSaveVO.getId())
                .eq(TrendRecord::getLotteryId, trendRecordSaveVO.getLotteryId())
                .eq(TrendRecord::getIssue, trendRecordSaveVO.getIssue())
                .eq(TrendRecord::getData, trendRecordSaveVO.getData())
                .eq(TrendRecord::getOpenTime, trendRecordSaveVO.getOpenTime())
                .eq(TrendRecord::getCreateTime, trendRecordSaveVO.getCreateTime())
        );
        if (count > 0) {
            throw new TrendRecordException(BaseException.BaseExceptionEnum.Exists);
        }

        TrendRecord newTrendRecord = new TrendRecord();
        BeanUtils.copyProperties(trendRecordSaveVO, newTrendRecord);

        trendRecordService.save(newTrendRecord);

        trendRecordSaveVO = new TrendRecordSaveVO();
        BeanUtils.copyProperties(newTrendRecord, trendRecordSaveVO);
        log.debug(JSON.toJSONString(trendRecordSaveVO));
        return trendRecordSaveVO;
    }


    /**
     * 统计趋势图
     *
     * @param groupId 群id
     * @return TrendRecordVO
     */
    @ApiOperation(value = "群id", notes = "群id")
    @GetMapping("/count/{groupId}")
    public R count(@PathVariable java.lang.String groupId) {
        List<TrendRecord> trendRecordList = trendRecordService.list(new LambdaQueryWrapper<TrendRecord>()
                .ge(TrendRecord::getCreateTime, DateUtil.offset(new Date(), DateField.HOUR, -1))
                .orderByDesc(TrendRecord::getId));
        if (CollectionUtils.isEmpty(trendRecordList)) {
            return null;
        }

        List<List<String>> arrayList = new ArrayList<>();
        for (TrendRecord trendRecord : trendRecordList) {
            arrayList.add(Lists.newArrayList(trendRecord.getOpenTime(), trendRecord.getData().split("\\|")[0]));
        }
        return R.success(arrayList);
    }


    /**
     * 查询走势记录信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询TrendRecord信息集合", notes = "查询TrendRecord信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
            @ApiImplicitParam(name = "openTimeBegin", value = "开奖时间", paramType = "query"),
            @ApiImplicitParam(name = "openTimeEnd", value = "开奖时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeBegin", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeEnd", value = "创建时间", paramType = "query")
    })
    @GetMapping(value = "/list")
    public IPage<TrendRecordPageVO> list(@ApiIgnore TrendRecordPageVO trendRecordVO, Integer curPage, Integer pageSize) {
        IPage<TrendRecord> page = new Page<>(curPage, pageSize);
        QueryWrapper<TrendRecord> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(trendRecordVO.getLotteryId())) {
            queryWrapper.lambda().eq(TrendRecord::getLotteryId, trendRecordVO.getLotteryId());
        }

        if (trendRecordVO.getCreateTimeEnd() != null) {
            queryWrapper.lambda().lt(TrendRecord::getCreateTime, trendRecordVO.getCreateTimeEnd());
        }
        int total = trendRecordService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(TrendRecord::getId);

            IPage<TrendRecord> trendRecordPage = trendRecordService.page(page, queryWrapper);
            List<TrendRecordPageVO> trendRecordPageVOList = JSON.parseArray(JSON.toJSONString(trendRecordPage.getRecords()), TrendRecordPageVO.class);
            IPage<TrendRecordPageVO> iPage = new Page<>();
            iPage.setPages(trendRecordPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(trendRecordPage.getTotal());
            iPage.setRecords(trendRecordPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 走势记录
     *
     * @return R
     */
    @ApiOperation(value = "修改TrendRecord", notes = "修改TrendRecord")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改TrendRecord", value = "传入json格式", required = true)
                          @RequestBody TrendRecordVO trendRecordVO) {
        if (StringUtils.isBlank(trendRecordVO.getId())) {
            throw new TrendRecordException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        TrendRecord newTrendRecord = new TrendRecord();
        BeanUtils.copyProperties(trendRecordVO, newTrendRecord);
        boolean isUpdated = trendRecordService.update(newTrendRecord, new LambdaQueryWrapper<TrendRecord>()
                .eq(TrendRecord::getId, trendRecordVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 走势记录
     *
     * @return R
     */
    @ApiOperation(value = "删除TrendRecord", notes = "删除TrendRecord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore TrendRecordVO trendRecordVO) {
        if (StringUtils.isBlank(trendRecordVO.getId())) {
            throw new TrendRecordException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        TrendRecord newTrendRecord = new TrendRecord();
        BeanUtils.copyProperties(trendRecordVO, newTrendRecord);
        trendRecordService.remove(new LambdaQueryWrapper<TrendRecord>()
                .eq(TrendRecord::getId, trendRecordVO.getId()));
        return R.success("删除成功");
    }

}
