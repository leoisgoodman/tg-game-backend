/*
 * tg学习代码
 */
package com.tggame.tg.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.core.entity.R;
import com.tggame.exceptions.BaseException;
import com.tggame.exceptions.TgUserFlowException;
import com.tggame.tg.entity.TgUserFlow;
import com.tggame.tg.service.TgUserFlowService;
import com.tggame.tg.vo.TgUserFlowPageVO;
import com.tggame.tg.vo.TgUserFlowSaveVO;
import com.tggame.tg.vo.TgUserFlowVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 用户流水
 *
 * @author tg
 */
@RestController
@RequestMapping("/tgUserFlow")
@Slf4j
@Api(value = "用户流水控制器", tags = "用户流水控制器")
public class TgUserFlowController {
    @Autowired
    private TgUserFlowService tgUserFlowService;


    /**
     * 创建 用户流水
     *
     * @return R
     */
    @ApiOperation(value = "创建TgUserFlow", notes = "创建TgUserFlow")
    @PostMapping("/build")
    public TgUserFlowSaveVO build(@ApiParam(name = "创建TgUserFlow", value = "传入json格式", required = true)
                                  @RequestBody TgUserFlowSaveVO tgUserFlowSaveVO) {
        if (StringUtils.isBlank(tgUserFlowSaveVO.getId())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(tgUserFlowSaveVO.getUserId())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(tgUserFlowSaveVO.getTgUsername())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(tgUserFlowSaveVO.getType())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(tgUserFlowSaveVO.getStatus())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(tgUserFlowSaveVO.getFrom())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(tgUserFlowSaveVO.getTo())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(tgUserFlowSaveVO.getSummary())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        int count = tgUserFlowService.count(new LambdaQueryWrapper<TgUserFlow>()
                .eq(TgUserFlow::getId, tgUserFlowSaveVO.getId())
                .eq(TgUserFlow::getUserId, tgUserFlowSaveVO.getUserId())
                .eq(TgUserFlow::getTgUsername, tgUserFlowSaveVO.getTgUsername())
                .eq(TgUserFlow::getAmount, tgUserFlowSaveVO.getAmount())
                .eq(TgUserFlow::getType, tgUserFlowSaveVO.getType())
                .eq(TgUserFlow::getStatus, tgUserFlowSaveVO.getStatus())
                .eq(TgUserFlow::getFrom, tgUserFlowSaveVO.getFrom())
                .eq(TgUserFlow::getTo, tgUserFlowSaveVO.getTo())
                .eq(TgUserFlow::getCreateTime, tgUserFlowSaveVO.getCreateTime())
                .eq(TgUserFlow::getUpdateTime, tgUserFlowSaveVO.getUpdateTime())
                .eq(TgUserFlow::getSummary, tgUserFlowSaveVO.getSummary())
        );
        if (count > 0) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Exists);
        }

        TgUserFlow newTgUserFlow = new TgUserFlow();
        BeanUtils.copyProperties(tgUserFlowSaveVO, newTgUserFlow);

        tgUserFlowService.save(newTgUserFlow);

        tgUserFlowSaveVO = new TgUserFlowSaveVO();
        BeanUtils.copyProperties(newTgUserFlow, tgUserFlowSaveVO);
        log.debug(JSON.toJSONString(tgUserFlowSaveVO));
        return tgUserFlowSaveVO;
    }


    /**
     * 查询用户流水信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询TgUserFlow信息集合", notes = "查询TgUserFlow信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
            @ApiImplicitParam(name = "createTimeBegin", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeEnd", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeBegin", value = "更新时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeEnd", value = "更新时间", paramType = "query"),
    })
    @GetMapping(value = "/list")
    public IPage<TgUserFlowPageVO> list(@ApiIgnore TgUserFlowPageVO tgUserFlowVO, Integer curPage, Integer pageSize) {
        IPage<TgUserFlow> page = new Page<>(curPage, pageSize);
        QueryWrapper<TgUserFlow> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(tgUserFlowVO.getUserId())) {
            queryWrapper.lambda().eq(TgUserFlow::getUserId, tgUserFlowVO.getUserId());
        }
        if (StringUtils.isNotBlank(tgUserFlowVO.getTgUsername())) {
            queryWrapper.lambda().eq(TgUserFlow::getTgUsername, tgUserFlowVO.getTgUsername());
        }
        if (StringUtils.isNotBlank(tgUserFlowVO.getType())) {
            queryWrapper.lambda().eq(TgUserFlow::getType, tgUserFlowVO.getType());
        }
        if (StringUtils.isNotBlank(tgUserFlowVO.getStatus())) {
            queryWrapper.lambda().eq(TgUserFlow::getStatus, tgUserFlowVO.getStatus());
        }
        if (StringUtils.isNotBlank(tgUserFlowVO.getFrom())) {
            queryWrapper.lambda().eq(TgUserFlow::getFrom, tgUserFlowVO.getFrom());
        }
        if (StringUtils.isNotBlank(tgUserFlowVO.getTo())) {
            queryWrapper.lambda().eq(TgUserFlow::getTo, tgUserFlowVO.getTo());
        }
        if (tgUserFlowVO.getCreateTimeBegin() != null) {
            queryWrapper.lambda().gt(TgUserFlow::getCreateTime, tgUserFlowVO.getCreateTimeBegin());
        }
        if (tgUserFlowVO.getCreateTimeEnd() != null) {
            queryWrapper.lambda().lt(TgUserFlow::getCreateTime, tgUserFlowVO.getCreateTimeEnd());
        }
        if (tgUserFlowVO.getUpdateTimeBegin() != null) {
            queryWrapper.lambda().gt(TgUserFlow::getUpdateTime, tgUserFlowVO.getUpdateTimeBegin());
        }
        if (tgUserFlowVO.getUpdateTimeEnd() != null) {
            queryWrapper.lambda().lt(TgUserFlow::getUpdateTime, tgUserFlowVO.getUpdateTimeEnd());
        }
        int total = tgUserFlowService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(TgUserFlow::getId);

            IPage<TgUserFlow> tgUserFlowPage = tgUserFlowService.page(page, queryWrapper);
            List<TgUserFlowPageVO> tgUserFlowPageVOList = JSON.parseArray(JSON.toJSONString(tgUserFlowPage.getRecords()), TgUserFlowPageVO.class);
            IPage<TgUserFlowPageVO> iPage = new Page<>();
            iPage.setPages(tgUserFlowPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(tgUserFlowPage.getTotal());
            iPage.setRecords(tgUserFlowPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 用户流水
     *
     * @return R
     */
    @ApiOperation(value = "修改TgUserFlow", notes = "修改TgUserFlow")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改TgUserFlow", value = "传入json格式", required = true)
                          @RequestBody TgUserFlowVO tgUserFlowVO) {
        if (StringUtils.isBlank(tgUserFlowVO.getId())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        TgUserFlow newTgUserFlow = new TgUserFlow();
        BeanUtils.copyProperties(tgUserFlowVO, newTgUserFlow);
        boolean isUpdated = tgUserFlowService.update(newTgUserFlow, new LambdaQueryWrapper<TgUserFlow>()
                .eq(TgUserFlow::getId, tgUserFlowVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 用户流水
     *
     * @return R
     */
    @ApiOperation(value = "删除TgUserFlow", notes = "删除TgUserFlow")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore TgUserFlowVO tgUserFlowVO) {
        if (StringUtils.isBlank(tgUserFlowVO.getId())) {
            throw new TgUserFlowException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        TgUserFlow newTgUserFlow = new TgUserFlow();
        BeanUtils.copyProperties(tgUserFlowVO, newTgUserFlow);
        tgUserFlowService.remove(new LambdaQueryWrapper<TgUserFlow>()
                .eq(TgUserFlow::getId, tgUserFlowVO.getId()));
        return R.success("删除成功");
    }

}
