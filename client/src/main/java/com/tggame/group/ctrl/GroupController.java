/*
 * tg学习代码
 */
package com.tggame.group.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.exceptions.GroupException;
import com.tggame.group.entity.Group;
import com.tggame.group.service.GroupService;
import com.tggame.group.vo.GroupPageVO;
import com.tggame.group.vo.GroupSaveVO;
import com.tggame.group.vo.GroupVO;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * tg群
 *
 * @author tg
 */
@RestController
@RequestMapping("/group")
@Slf4j
@Api(value = "tg群控制器", tags = "tg群控制器")
public class GroupController {
    @Autowired
    private GroupService groupService;


    /**
     * 创建 tg群
     *
     * @return R
     */
    @ApiOperation(value = "创建Group", notes = "创建Group")
    @PostMapping("/build")
    public GroupSaveVO build(@ApiParam(name = "创建Group", value = "传入json格式", required = true)
                             @RequestBody GroupSaveVO groupSaveVO) {
        if (StringUtils.isBlank(groupSaveVO.getId())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupSaveVO.getMerchantId())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupSaveVO.getName())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupSaveVO.getTgGroupId())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupSaveVO.getTgGameCode())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupSaveVO.getDomain())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupSaveVO.getStatus())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        if (StringUtils.isBlank(groupSaveVO.getSummary())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupSaveVO.getGameSummary())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        int count = groupService.count(new LambdaQueryWrapper<Group>()
                .eq(Group::getId, groupSaveVO.getId())
                .eq(Group::getMerchantId, groupSaveVO.getMerchantId())
                .eq(Group::getName, groupSaveVO.getName())
                .eq(Group::getTgGroupId, groupSaveVO.getTgGroupId())
                .eq(Group::getTgGameCode, groupSaveVO.getTgGameCode())
                .eq(Group::getDomain, groupSaveVO.getDomain())
                .eq(Group::getStatus, groupSaveVO.getStatus())
                .eq(Group::getCreateTime, groupSaveVO.getCreateTime())
                .eq(Group::getSummary, groupSaveVO.getSummary())
                .eq(Group::getGameSummary, groupSaveVO.getGameSummary())
        );
        if (count > 0) {
            throw new GroupException(BaseException.BaseExceptionEnum.Exists);
        }

        Group newGroup = new Group();
        BeanUtils.copyProperties(groupSaveVO, newGroup);

        groupService.save(newGroup);

        groupSaveVO = new GroupSaveVO();
        BeanUtils.copyProperties(newGroup, groupSaveVO);
        log.debug(JSON.toJSONString(groupSaveVO));
        return groupSaveVO;
    }


    /**
     * 根据条件tgGroupId查询tg群一个详情信息
     *
     * @param tgGroupId tg群id
     * @return GroupVO
     */
    @ApiOperation(value = "创建Group", notes = "创建Group")
    @GetMapping("/load/tgGroupId/{tgGroupId}")
    public GroupVO loadByTgGroupId(@PathVariable java.lang.String tgGroupId) {
        Group group = groupService.getOne(new LambdaQueryWrapper<Group>()
                .eq(Group::getTgGroupId, tgGroupId));
        GroupVO groupVO = new GroupVO();
        BeanUtils.copyProperties(group, groupVO);
        log.debug(JSON.toJSONString(groupVO));
        return groupVO;
    }

    /**
     * 查询tg群信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询Group信息集合", notes = "查询Group信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
            @ApiImplicitParam(name = "createTimeBegin", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeEnd", value = "创建时间", paramType = "query"),
    })
    @GetMapping(value = "/list")
    public IPage<GroupPageVO> list(@ApiIgnore GroupPageVO groupVO, Integer curPage, Integer pageSize) {
        IPage<Group> page = new Page<>(curPage, pageSize);
        QueryWrapper<Group> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(groupVO.getTgGroupId())) {
            queryWrapper.lambda().eq(Group::getTgGroupId, groupVO.getTgGroupId());
        }
        if (StringUtils.isNotBlank(groupVO.getTgGameCode())) {
            queryWrapper.lambda().eq(Group::getTgGameCode, groupVO.getTgGameCode());
        }
        if (StringUtils.isNotBlank(groupVO.getStatus())) {
            queryWrapper.lambda().eq(Group::getStatus, groupVO.getStatus());
        }
        if (groupVO.getCreateTimeBegin() != null) {
            queryWrapper.lambda().gt(Group::getCreateTime, groupVO.getCreateTimeBegin());
        }
        if (groupVO.getCreateTimeEnd() != null) {
            queryWrapper.lambda().lt(Group::getCreateTime, groupVO.getCreateTimeEnd());
        }
        int total = groupService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(Group::getId);

            IPage<Group> groupPage = groupService.page(page, queryWrapper);
            List<GroupPageVO> groupPageVOList = JSON.parseArray(JSON.toJSONString(groupPage.getRecords()), GroupPageVO.class);
            IPage<GroupPageVO> iPage = new Page<>();
            iPage.setPages(groupPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(groupPage.getTotal());
            iPage.setRecords(groupPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 tg群
     *
     * @return R
     */
    @ApiOperation(value = "修改Group", notes = "修改Group")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改Group", value = "传入json格式", required = true)
                          @RequestBody GroupVO groupVO) {
        if (StringUtils.isBlank(groupVO.getId())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Group newGroup = new Group();
        BeanUtils.copyProperties(groupVO, newGroup);
        boolean isUpdated = groupService.update(newGroup, new LambdaQueryWrapper<Group>()
                .eq(Group::getId, groupVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 tg群
     *
     * @return R
     */
    @ApiOperation(value = "删除Group", notes = "删除Group")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
            @ApiImplicitParam(name = "tgGroupId", value = "tg群id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore GroupVO groupVO) {
        if (StringUtils.isBlank(groupVO.getId())) {
            throw new GroupException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Group newGroup = new Group();
        BeanUtils.copyProperties(groupVO, newGroup);
        groupService.remove(new LambdaQueryWrapper<Group>()
                .eq(Group::getId, groupVO.getId()));
        return R.success("删除成功");
    }

}
