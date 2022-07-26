/*
 * tg学习代码
 */
package com.tggame.trend.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
            queryWrapper.lambda().orderByDesc(TrendRecord::getCreateTime);

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

}
