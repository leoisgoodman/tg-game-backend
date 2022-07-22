/*
 * tg学习代码
 */
package com.tggame.group.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.core.entity.R;
import com.tggame.exceptions.BaseException;
import com.tggame.exceptions.GroupBetException;
import com.tggame.group.entity.GroupBet;
import com.tggame.group.service.GroupBetService;
import com.tggame.group.vo.GroupBetPageVO;
import com.tggame.group.vo.GroupBetSaveVO;
import com.tggame.group.vo.GroupBetVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 群玩法
 *
 * @author tg
 */
@RestController
@RequestMapping("/groupBet")
@Slf4j
@Api(value = "群玩法控制器", tags = "群玩法控制器")
public class GroupBetController {
    @Autowired
    private GroupBetService groupBetService;


    /**
     * 创建 群玩法
     *
     * @return R
     */
    @ApiOperation(value = "创建GroupBet", notes = "创建GroupBet")
    @PostMapping("/build")
    public GroupBetSaveVO build(@ApiParam(name = "创建GroupBet", value = "传入json格式", required = true)
                                @RequestBody GroupBetSaveVO groupBetSaveVO) {
        if (StringUtils.isBlank(groupBetSaveVO.getId())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupBetSaveVO.getGroupId())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupBetSaveVO.getGroupLotteryId())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupBetSaveVO.getBetId())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupBetSaveVO.getCode())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(groupBetSaveVO.getType())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(groupBetSaveVO.getStatus())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        int count = groupBetService.count(new LambdaQueryWrapper<GroupBet>()
                .eq(GroupBet::getId, groupBetSaveVO.getId())
                .eq(GroupBet::getGroupId, groupBetSaveVO.getGroupId())
                .eq(GroupBet::getGroupLotteryId, groupBetSaveVO.getGroupLotteryId())
                .eq(GroupBet::getBetId, groupBetSaveVO.getBetId())
                .eq(GroupBet::getCode, groupBetSaveVO.getCode())
                .eq(GroupBet::getMinBetMoney, groupBetSaveVO.getMinBetMoney())
                .eq(GroupBet::getMaxBetMoney, groupBetSaveVO.getMaxBetMoney())
                .eq(GroupBet::getOdds, groupBetSaveVO.getOdds())
                .eq(GroupBet::getDefaultMoney, groupBetSaveVO.getDefaultMoney())
                .eq(GroupBet::getType, groupBetSaveVO.getType())
                .eq(GroupBet::getPayBackPercent, groupBetSaveVO.getPayBackPercent())
                .eq(GroupBet::getStatus, groupBetSaveVO.getStatus())
        );
        if (count > 0) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Exists);
        }

        GroupBet newGroupBet = new GroupBet();
        BeanUtils.copyProperties(groupBetSaveVO, newGroupBet);

        groupBetService.save(newGroupBet);

        groupBetSaveVO = new GroupBetSaveVO();
        BeanUtils.copyProperties(newGroupBet, groupBetSaveVO);
        log.debug(JSON.toJSONString(groupBetSaveVO));
        return groupBetSaveVO;
    }


    /**
     * 根据条件code查询群玩法一个详情信息
     *
     * @param code 编码
     * @return GroupBetVO
     */
    @ApiOperation(value = "创建GroupBet", notes = "创建GroupBet")
    @GetMapping("/load/code/{code}")
    public GroupBetVO loadByCode(@PathVariable java.lang.String code) {
        GroupBet groupBet = groupBetService.getOne(new LambdaQueryWrapper<GroupBet>()
                .eq(GroupBet::getCode, code));
        GroupBetVO groupBetVO = new GroupBetVO();
        BeanUtils.copyProperties(groupBet, groupBetVO);
        log.debug(JSON.toJSONString(groupBetVO));
        return groupBetVO;
    }

    /**
     * 查询群玩法信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询GroupBet信息集合", notes = "查询GroupBet信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
    })
    @GetMapping(value = "/list")
    public IPage<GroupBetPageVO> list(@ApiIgnore GroupBetPageVO groupBetVO, Integer curPage, Integer pageSize) {
        IPage<GroupBet> page = new Page<>(curPage, pageSize);
        QueryWrapper<GroupBet> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(groupBetVO.getGroupId())) {
            queryWrapper.lambda().eq(GroupBet::getGroupId, groupBetVO.getGroupId());
        }
        if (StringUtils.isNotBlank(groupBetVO.getGroupLotteryId())) {
            queryWrapper.lambda().eq(GroupBet::getGroupLotteryId, groupBetVO.getGroupLotteryId());
        }
        if (StringUtils.isNotBlank(groupBetVO.getBetId())) {
            queryWrapper.lambda().eq(GroupBet::getBetId, groupBetVO.getBetId());
        }

        if (StringUtils.isNotBlank(groupBetVO.getType())) {
            queryWrapper.lambda().eq(GroupBet::getType, groupBetVO.getType());
        }
        if (StringUtils.isNotBlank(groupBetVO.getStatus())) {
            queryWrapper.lambda().eq(GroupBet::getStatus, groupBetVO.getStatus());
        }
        int total = groupBetService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(GroupBet::getId);

            IPage<GroupBet> groupBetPage = groupBetService.page(page, queryWrapper);
            List<GroupBetPageVO> groupBetPageVOList = JSON.parseArray(JSON.toJSONString(groupBetPage.getRecords()), GroupBetPageVO.class);
            IPage<GroupBetPageVO> iPage = new Page<>();
            iPage.setPages(groupBetPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(groupBetPage.getTotal());
            iPage.setRecords(groupBetPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 群玩法
     *
     * @return R
     */
    @ApiOperation(value = "修改GroupBet", notes = "修改GroupBet")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改GroupBet", value = "传入json格式", required = true)
                          @RequestBody GroupBetVO groupBetVO) {
        if (StringUtils.isBlank(groupBetVO.getId())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        GroupBet newGroupBet = new GroupBet();
        BeanUtils.copyProperties(groupBetVO, newGroupBet);
        boolean isUpdated = groupBetService.update(newGroupBet, new LambdaQueryWrapper<GroupBet>()
                .eq(GroupBet::getId, groupBetVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 群玩法
     *
     * @return R
     */
    @ApiOperation(value = "删除GroupBet", notes = "删除GroupBet")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "编码", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore GroupBetVO groupBetVO) {
        if (StringUtils.isBlank(groupBetVO.getId())) {
            throw new GroupBetException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        GroupBet newGroupBet = new GroupBet();
        BeanUtils.copyProperties(groupBetVO, newGroupBet);
        groupBetService.remove(new LambdaQueryWrapper<GroupBet>()
                .eq(GroupBet::getId, groupBetVO.getId()));
        return R.success("删除成功");
    }

}
