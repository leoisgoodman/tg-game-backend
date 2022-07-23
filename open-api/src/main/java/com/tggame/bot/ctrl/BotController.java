/*
 * tg学习代码
 */
package com.tggame.bot.ctrl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.tggame.bot.entity.Bot;
import com.tggame.bot.service.BotService;
import com.tggame.bot.vo.BotPageVO;
import com.tggame.bot.vo.BotSaveVO;
import com.tggame.bot.vo.BotVO;
import com.tggame.core.base.BaseException;
import com.tggame.core.entity.R;
import com.tggame.exceptions.BotException;
import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.List;

/**
 * tg机器人
 *
 * @author tg
 */
@RestController
@RequestMapping("/bot")
@Slf4j
@Api(value = "tg机器人控制器", tags = "tg机器人控制器")
public class BotController {
    @Autowired
    private BotService botService;


    /**
     * 创建 tg机器人
     *
     * @return R
     */
    @ApiOperation(value = "创建Bot", notes = "创建Bot")
    @PostMapping("/build")
    public BotSaveVO build(@ApiParam(name = "创建Bot", value = "传入json格式", required = true)
                           @RequestBody BotSaveVO botSaveVO) {
        if (StringUtils.isBlank(botSaveVO.getId())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(botSaveVO.getTgBotId())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(botSaveVO.getName())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(botSaveVO.getTgToken())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(botSaveVO.getBotOwnerUsername())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(botSaveVO.getTgPhone())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(botSaveVO.getShareLink())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(botSaveVO.getStatus())) {
            throw new BotException(BaseException.BaseExceptionEnum.Empty_Param);
        }


        int count = botService.count(new LambdaQueryWrapper<Bot>()
                .eq(Bot::getId, botSaveVO.getId())
                .eq(Bot::getTgBotId, botSaveVO.getTgBotId())
                .eq(Bot::getName, botSaveVO.getName())
                .eq(Bot::getTgToken, botSaveVO.getTgToken())
                .eq(Bot::getBotOwnerUsername, botSaveVO.getBotOwnerUsername())
                .eq(Bot::getTgPhone, botSaveVO.getTgPhone())
                .eq(Bot::getShareLink, botSaveVO.getShareLink())
                .eq(Bot::getStatus, botSaveVO.getStatus())
                .eq(Bot::getCreateTime, botSaveVO.getCreateTime())
                .eq(Bot::getUpdateTime, botSaveVO.getUpdateTime())
        );
        if (count > 0) {
            throw new BotException(BaseException.BaseExceptionEnum.Exists);
        }

        Bot newBot = new Bot();
        BeanUtils.copyProperties(botSaveVO, newBot);

        botService.save(newBot);

        botSaveVO = new BotSaveVO();
        BeanUtils.copyProperties(newBot, botSaveVO);
        log.debug(JSON.toJSONString(botSaveVO));
        return botSaveVO;
    }


    /**
     * 根据条件tgBotId查询tg机器人一个详情信息
     *
     * @param tgBotId tg 机器人id
     * @return BotVO
     */
    @ApiOperation(value = "创建Bot", notes = "创建Bot")
    @GetMapping("/load/tgBotId/{tgBotId}")
    public BotVO loadByTgBotId(@PathVariable java.lang.String tgBotId) {
        Bot bot = botService.getOne(new LambdaQueryWrapper<Bot>()
                .eq(Bot::getTgBotId, tgBotId));
        BotVO botVO = new BotVO();
        BeanUtils.copyProperties(bot, botVO);
        log.debug(JSON.toJSONString(botVO));
        return botVO;
    }


    /**
     * 根据条件 name 查询tg机器人一个详情信息
     *
     * @return BotVO
     */
    @ApiOperation(value = "根据条件 name 查询tg机器人一个详情信息", notes = "根据条件 name 查询tg机器人一个详情信息")
    @GetMapping("/load/name/{name}")
    public BotVO loadByName(@PathVariable java.lang.String name) {
        Bot bot = botService.getOne(new LambdaQueryWrapper<Bot>()
                .eq(Bot::getName, name));
        BotVO botVO = new BotVO();
        BeanUtils.copyProperties(bot, botVO);
        log.debug("查詢機器人結果-{}", botVO);
        return botVO;
    }

    /**
     * 查询tg机器人信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询Bot信息集合", notes = "查询Bot信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
            @ApiImplicitParam(name = "createTimeBegin", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "createTimeEnd", value = "创建时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeBegin", value = "更新时间", paramType = "query"),
            @ApiImplicitParam(name = "updateTimeEnd", value = "更新时间", paramType = "query")
    })
    @GetMapping(value = "/list")
    public IPage<BotPageVO> list(@ApiIgnore BotPageVO botVO, Integer curPage, Integer pageSize) {
        IPage<Bot> page = new Page<>(curPage, pageSize);
        QueryWrapper<Bot> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(botVO.getTgBotId())) {
            queryWrapper.lambda().eq(Bot::getTgBotId, botVO.getTgBotId());
        }
        if (StringUtils.isNotBlank(botVO.getName())) {
            queryWrapper.lambda().eq(Bot::getName, botVO.getName());
        }
        if (StringUtils.isNotBlank(botVO.getTgToken())) {
            queryWrapper.lambda().eq(Bot::getTgToken, botVO.getTgToken());
        }
        if (StringUtils.isNotBlank(botVO.getBotOwnerUsername())) {
            queryWrapper.lambda().eq(Bot::getBotOwnerUsername, botVO.getBotOwnerUsername());
        }
        if (StringUtils.isNotBlank(botVO.getStatus())) {
            queryWrapper.lambda().eq(Bot::getStatus, botVO.getStatus());
        }
        if (botVO.getCreateTimeBegin() != null) {
            queryWrapper.lambda().gt(Bot::getCreateTime, botVO.getCreateTimeBegin());
        }
        if (botVO.getCreateTimeEnd() != null) {
            queryWrapper.lambda().lt(Bot::getCreateTime, botVO.getCreateTimeEnd());
        }
        if (botVO.getUpdateTimeBegin() != null) {
            queryWrapper.lambda().gt(Bot::getUpdateTime, botVO.getUpdateTimeBegin());
        }
        if (botVO.getUpdateTimeEnd() != null) {
            queryWrapper.lambda().lt(Bot::getUpdateTime, botVO.getUpdateTimeEnd());
        }
        int total = botService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(Bot::getId);

            IPage<Bot> botPage = botService.page(page, queryWrapper);
            List<BotPageVO> botPageVOList = JSON.parseArray(JSON.toJSONString(botPage.getRecords()), BotPageVO.class);
            IPage<BotPageVO> iPage = new Page<>();
            iPage.setPages(botPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(botPage.getTotal());
            iPage.setRecords(botPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 tg机器人
     *
     * @return R
     */
    @ApiOperation(value = "修改Bot", notes = "修改Bot")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改Bot", value = "传入json格式", required = true)
                          @RequestBody BotVO botVO) {
        if (StringUtils.isBlank(botVO.getId())) {
            throw new BotException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Bot newBot = new Bot();
        BeanUtils.copyProperties(botVO, newBot);
        boolean isUpdated = botService.update(newBot, new LambdaQueryWrapper<Bot>()
                .eq(Bot::getId, botVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 tg机器人
     *
     * @return R
     */
    @ApiOperation(value = "删除Bot", notes = "删除Bot")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
            @ApiImplicitParam(name = "tgBotId", value = "tg 机器人id", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore BotVO botVO) {
        if (StringUtils.isBlank(botVO.getId())) {
            throw new BotException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Bot newBot = new Bot();
        BeanUtils.copyProperties(botVO, newBot);
        botService.remove(new LambdaQueryWrapper<Bot>()
                .eq(Bot::getId, botVO.getId()));
        return R.success("删除成功");
    }

}
