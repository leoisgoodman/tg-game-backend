/*
 * tg学习代码
 */
package com.tggame.open.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.core.entity.R;
import com.tggame.exceptions.BaseException;
import com.tggame.exceptions.OpenRecordException;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.service.OpenRecordService;
import com.tggame.open.vo.OpenRecordPageVO;
import com.tggame.open.vo.OpenRecordSaveVO;
import com.tggame.open.vo.OpenRecordVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 开奖记录
 *
 * @author tg
 */
@RestController
@RequestMapping("/openRecord")
@Slf4j
@Api(value = "开奖记录控制器", tags = "开奖记录控制器")
public class OpenRecordController {
    @Autowired
    private OpenRecordService openRecordService;


    /**
     * 创建 开奖记录
     *
     * @return R
     */
    @ApiOperation(value = "创建OpenRecord", notes = "创建OpenRecord")
    @PostMapping("/build")
    public OpenRecordSaveVO build(@ApiParam(name = "创建OpenRecord", value = "传入json格式", required = true)
                                  @RequestBody OpenRecordSaveVO openRecordSaveVO) {
        if (StringUtils.isBlank(openRecordSaveVO.getId())) {
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(openRecordSaveVO.getLotteryId())) {
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(openRecordSaveVO.getNum())) {
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(openRecordSaveVO.getStatus())) {
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Empty_Param);
        }


        int count = openRecordService.count(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getId, openRecordSaveVO.getId())
                .eq(OpenRecord::getLotteryId, openRecordSaveVO.getLotteryId())
                .eq(OpenRecord::getIssue, openRecordSaveVO.getIssue())
                .eq(OpenRecord::getNum, openRecordSaveVO.getNum())
                .eq(OpenRecord::getStatus, openRecordSaveVO.getStatus())
                .eq(OpenRecord::getCreateTime, openRecordSaveVO.getCreateTime())
                .eq(OpenRecord::getUpdateTime, openRecordSaveVO.getUpdateTime())
        );
        if (count > 0) {
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Exists);
        }

        OpenRecord newOpenRecord = new OpenRecord();
        BeanUtils.copyProperties(openRecordSaveVO, newOpenRecord);

        openRecordService.save(newOpenRecord);

        openRecordSaveVO = new OpenRecordSaveVO();
        BeanUtils.copyProperties(newOpenRecord, openRecordSaveVO);
        log.debug(JSON.toJSONString(openRecordSaveVO));
        return openRecordSaveVO;
    }


    /**
     * 查询开奖记录信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询OpenRecord信息集合", notes = "查询OpenRecord信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
            @ApiImplicitParam(name = "createTimeBegin", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeEnd", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeBegin", value = "更新时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeEnd", value = "更新时间", paramType = "query")
    })
    @GetMapping(value = "/list")
    public IPage<OpenRecordPageVO> list(@ApiIgnore OpenRecordPageVO openRecordVO, Integer curPage, Integer pageSize) {
        IPage<OpenRecord> page = new Page<>(curPage, pageSize);
        QueryWrapper<OpenRecord> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(openRecordVO.getLotteryId())) {
            queryWrapper.lambda().eq(OpenRecord::getLotteryId, openRecordVO.getLotteryId());
        }
        if (StringUtils.isNotBlank(openRecordVO.getStatus())) {
            queryWrapper.lambda().eq(OpenRecord::getStatus, openRecordVO.getStatus());
        }
        if (openRecordVO.getCreateTimeBegin() != null) {
            queryWrapper.lambda().gt(OpenRecord::getCreateTime, openRecordVO.getCreateTimeBegin());
        }
        if (openRecordVO.getCreateTimeEnd() != null) {
            queryWrapper.lambda().lt(OpenRecord::getCreateTime, openRecordVO.getCreateTimeEnd());
        }
        if (openRecordVO.getUpdateTimeBegin() != null) {
            queryWrapper.lambda().gt(OpenRecord::getUpdateTime, openRecordVO.getUpdateTimeBegin());
        }
        if (openRecordVO.getUpdateTimeEnd() != null) {
            queryWrapper.lambda().lt(OpenRecord::getUpdateTime, openRecordVO.getUpdateTimeEnd());
        }
        int total = openRecordService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(OpenRecord::getId);

            IPage<OpenRecord> openRecordPage = openRecordService.page(page, queryWrapper);
            List<OpenRecordPageVO> openRecordPageVOList = JSON.parseArray(JSON.toJSONString(openRecordPage.getRecords()), OpenRecordPageVO.class);
            IPage<OpenRecordPageVO> iPage = new Page<>();
            iPage.setPages(openRecordPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(openRecordPage.getTotal());
            iPage.setRecords(openRecordPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 开奖记录
     *
     * @return R
     */
    @ApiOperation(value = "修改OpenRecord", notes = "修改OpenRecord")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改OpenRecord", value = "传入json格式", required = true)
                          @RequestBody OpenRecordVO openRecordVO) {
        if (StringUtils.isBlank(openRecordVO.getId())) {
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        OpenRecord newOpenRecord = new OpenRecord();
        BeanUtils.copyProperties(openRecordVO, newOpenRecord);
        boolean isUpdated = openRecordService.update(newOpenRecord, new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getId, openRecordVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 开奖记录
     *
     * @return R
     */
    @ApiOperation(value = "删除OpenRecord", notes = "删除OpenRecord")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore OpenRecordVO openRecordVO) {
        if (StringUtils.isBlank(openRecordVO.getId())) {
            throw new OpenRecordException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        OpenRecord newOpenRecord = new OpenRecord();
        BeanUtils.copyProperties(openRecordVO, newOpenRecord);
        openRecordService.remove(new LambdaQueryWrapper<OpenRecord>()
                .eq(OpenRecord::getId, openRecordVO.getId()));
        return R.success("删除成功");
    }

}
