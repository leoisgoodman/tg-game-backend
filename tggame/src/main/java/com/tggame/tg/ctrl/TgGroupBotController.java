/*
 * tg学习代码
 */
package com.tggame.tg.ctrl;

import com.tggame.tg.entity.TgGroupBot;
import com.tggame.tg.service.TgGroupBotService;
import com.tggame.tg.vo.TgGroupBotPageVO;
import com.tggame.tg.vo.TgGroupBotSaveVO;
import com.tggame.tg.vo.TgGroupBotVO;
import com.tggame.core.exceptions.TgGroupBotException;
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
 * 群机器人
 *
 * @author tg
 */
@RestController
@RequestMapping("/tgGroupBot")
@Slf4j
@Api(value = "群机器人控制器", tags = "群机器人控制器")
public class TgGroupBotController {
    @Autowired
    private TgGroupBotService tgGroupBotService;


    /**
     * 创建 群机器人
     *
     * @return R
     */
    @ApiOperation(value = "创建TgGroupBot", notes = "创建TgGroupBot")
    @PostMapping("/build")
    public TgGroupBotSaveVO build(@ApiParam(name = "创建TgGroupBot", value = "传入json格式", required = true)
                                   @RequestBody TgGroupBotSaveVO tgGroupBotSaveVO) {
        if (StringUtils.isBlank(tgGroupBotSaveVO.getId())) {
            throw new TgGroupBotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(tgGroupBotSaveVO.getGroupId())) {
            throw new TgGroupBotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(tgGroupBotSaveVO.getBotId())) {
            throw new TgGroupBotException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        int count = tgGroupBotService.count(new LambdaQueryWrapper<TgGroupBot>()
                .eq(TgGroupBot::getId, tgGroupBotSaveVO.getId())
                        .eq(TgGroupBot::getGroupId, tgGroupBotSaveVO.getGroupId())
                        .eq(TgGroupBot::getBotId, tgGroupBotSaveVO.getBotId())
        );
        if (count > 0) {
            throw new TgGroupBotException(BaseException.BaseExceptionEnum.Exists);
        }

        TgGroupBot newTgGroupBot = new TgGroupBot();
        BeanUtils.copyProperties(tgGroupBotSaveVO, newTgGroupBot);

        tgGroupBotService.save(newTgGroupBot);

        tgGroupBotSaveVO = new TgGroupBotSaveVO();
        BeanUtils.copyProperties(newTgGroupBot, tgGroupBotSaveVO);
        log.debug(JSON.toJSONString(tgGroupBotSaveVO));
        return tgGroupBotSaveVO;
    }



    /**
     * 查询群机器人信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询TgGroupBot信息集合", notes = "查询TgGroupBot信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
    })
    @GetMapping(value = "/list")
    public IPage<TgGroupBotPageVO> list(@ApiIgnore TgGroupBotPageVO tgGroupBotVO, Integer curPage, Integer pageSize) {
        IPage<TgGroupBot> page = new Page<>(curPage, pageSize);
        QueryWrapper<TgGroupBot> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(tgGroupBotVO.getGroupId())) {
            queryWrapper.lambda().eq(TgGroupBot::getGroupId, tgGroupBotVO.getGroupId());
        }
        if (StringUtils.isNotBlank(tgGroupBotVO.getBotId())) {
            queryWrapper.lambda().eq(TgGroupBot::getBotId, tgGroupBotVO.getBotId());
        }
        int total = tgGroupBotService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(TgGroupBot::getId);

            IPage<TgGroupBot> tgGroupBotPage = tgGroupBotService.page(page,queryWrapper);
            List<TgGroupBotPageVO> tgGroupBotPageVOList = JSON.parseArray(JSON.toJSONString(tgGroupBotPage.getRecords()), TgGroupBotPageVO.class);
            IPage<TgGroupBotPageVO> iPage = new Page<>();
            iPage.setPages(tgGroupBotPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(tgGroupBotPage.getTotal());
            iPage.setRecords(tgGroupBotPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 群机器人
     *
     * @return R
     */
    @ApiOperation(value = "修改TgGroupBot", notes = "修改TgGroupBot")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改TgGroupBot", value = "传入json格式", required = true)
                          @RequestBody TgGroupBotVO tgGroupBotVO) {
        if (StringUtils.isBlank(tgGroupBotVO.getId())) {
            throw new TgGroupBotException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        TgGroupBot newTgGroupBot = new TgGroupBot();
        BeanUtils.copyProperties(tgGroupBotVO, newTgGroupBot);
        boolean isUpdated = tgGroupBotService.update(newTgGroupBot, new LambdaQueryWrapper<TgGroupBot>()
                .eq(TgGroupBot::getId, tgGroupBotVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 群机器人
     *
     * @return R
     */
    @ApiOperation(value = "删除TgGroupBot", notes = "删除TgGroupBot")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore TgGroupBotVO tgGroupBotVO) {
        if (StringUtils.isBlank(tgGroupBotVO.getId())) {
            throw new TgGroupBotException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        TgGroupBot newTgGroupBot = new TgGroupBot();
        BeanUtils.copyProperties(tgGroupBotVO, newTgGroupBot);
        tgGroupBotService.remove(new LambdaQueryWrapper<TgGroupBot>()
                .eq(TgGroupBot::getId, tgGroupBotVO.getId()));
        return R.success("删除成功");
    }

}
