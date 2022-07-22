/*
 * tg学习代码
 */
package com.tggame.group.ctrl;

import com.tggame.group.entity.GroupLottery;
import com.tggame.group.service.GroupLotteryService;
import com.tggame.group.vo.GroupLotteryPageVO;
import com.tggame.group.vo.GroupLotterySaveVO;
import com.tggame.group.vo.GroupLotteryVO;
import com.tggame.core.exceptions.GroupLotteryException;
import com.tggame.core.exceptions.BaseException;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.core.entity.PageVO;
import com.tggame.core.entity.R;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * 群彩种
 *
 * @author tg
 */
@RestController
@RequestMapping("/groupLottery")
@Slf4j
@Api(value = "群彩种控制器", tags = "群彩种控制器")
public class GroupLotteryController {
    @Autowired
    private GroupLotteryService groupLotteryService;


    /**
     * 创建 群彩种
     *
     * @return R
     */
    @ApiOperation(value = "创建GroupLottery", notes = "创建GroupLottery")
    @PostMapping("/build")
    public GroupLotterySaveVO build(@ApiParam(name = "创建GroupLottery", value = "传入json格式", required = true)
                                   @RequestBody GroupLotterySaveVO groupLotterySaveVO) {
        if (StringUtils.isBlank(groupLotterySaveVO.getId())) {
            throw new GroupLotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupLotterySaveVO.getGroupId())) {
            throw new GroupLotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(groupLotterySaveVO.getLotteryId())) {
            throw new GroupLotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        int count = groupLotteryService.count(new LambdaQueryWrapper<GroupLottery>()
                .eq(GroupLottery::getId, groupLotterySaveVO.getId())
                        .eq(GroupLottery::getGroupId, groupLotterySaveVO.getGroupId())
                        .eq(GroupLottery::getLotteryId, groupLotterySaveVO.getLotteryId())
        );
        if (count > 0) {
            throw new GroupLotteryException(BaseException.BaseExceptionEnum.Exists);
        }

        GroupLottery newGroupLottery = new GroupLottery();
        BeanUtils.copyProperties(groupLotterySaveVO, newGroupLottery);

        groupLotteryService.save(newGroupLottery);

        groupLotterySaveVO = new GroupLotterySaveVO();
        BeanUtils.copyProperties(newGroupLottery, groupLotterySaveVO);
        log.debug(JSON.toJSONString(groupLotterySaveVO));
        return groupLotterySaveVO;
    }



    /**
     * 查询群彩种信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询GroupLottery信息集合", notes = "查询GroupLottery信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
    })
    @GetMapping(value = "/list")
    public IPage<GroupLotteryPageVO> list(@ApiIgnore GroupLotteryPageVO groupLotteryVO, Integer curPage, Integer pageSize) {
        IPage<GroupLottery> page = new Page<>(curPage, pageSize);
        QueryWrapper<GroupLottery> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(groupLotteryVO.getGroupId())) {
            queryWrapper.lambda().eq(GroupLottery::getGroupId, groupLotteryVO.getGroupId());
        }
        if (StringUtils.isNotBlank(groupLotteryVO.getLotteryId())) {
            queryWrapper.lambda().eq(GroupLottery::getLotteryId, groupLotteryVO.getLotteryId());
        }
        int total = groupLotteryService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(GroupLottery::getId);

            IPage<GroupLottery> groupLotteryPage = groupLotteryService.page(page,queryWrapper);
            List<GroupLotteryPageVO> groupLotteryPageVOList = JSON.parseArray(JSON.toJSONString(groupLotteryPage.getRecords()), GroupLotteryPageVO.class);
            IPage<GroupLotteryPageVO> iPage = new Page<>();
            iPage.setPages(groupLotteryPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(groupLotteryPage.getTotal());
            iPage.setRecords(groupLotteryPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 群彩种
     *
     * @return R
     */
    @ApiOperation(value = "修改GroupLottery", notes = "修改GroupLottery")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改GroupLottery", value = "传入json格式", required = true)
                          @RequestBody GroupLotteryVO groupLotteryVO) {
        if (StringUtils.isBlank(groupLotteryVO.getId())) {
            throw new GroupLotteryException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        GroupLottery newGroupLottery = new GroupLottery();
        BeanUtils.copyProperties(groupLotteryVO, newGroupLottery);
        boolean isUpdated = groupLotteryService.update(newGroupLottery, new LambdaQueryWrapper<GroupLottery>()
                .eq(GroupLottery::getId, groupLotteryVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 群彩种
     *
     * @return R
     */
    @ApiOperation(value = "删除GroupLottery", notes = "删除GroupLottery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore GroupLotteryVO groupLotteryVO) {
        if (StringUtils.isBlank(groupLotteryVO.getId())) {
            throw new GroupLotteryException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        GroupLottery newGroupLottery = new GroupLottery();
        BeanUtils.copyProperties(groupLotteryVO, newGroupLottery);
        groupLotteryService.remove(new LambdaQueryWrapper<GroupLottery>()
                .eq(GroupLottery::getId, groupLotteryVO.getId()));
        return R.success("删除成功");
    }

}
