/*
 * tg学习代码
 */
package com.tggame.lottery.ctrl;

import com.tggame.lottery.entity.Lottery;
import com.tggame.lottery.service.LotteryService;
import com.tggame.lottery.vo.LotteryPageVO;
import com.tggame.lottery.vo.LotterySaveVO;
import com.tggame.lottery.vo.LotteryVO;
import com.tggame.core.exceptions.LotteryException;
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
 * 彩种
 *
 * @author tg
 */
@RestController
@RequestMapping("/lottery")
@Slf4j
@Api(value = "彩种控制器", tags = "彩种控制器")
public class LotteryController {
    @Autowired
    private LotteryService lotteryService;


    /**
     * 创建 彩种
     *
     * @return R
     */
    @ApiOperation(value = "创建Lottery", notes = "创建Lottery")
    @PostMapping("/build")
    public LotterySaveVO build(@ApiParam(name = "创建Lottery", value = "传入json格式", required = true)
                                   @RequestBody LotterySaveVO lotterySaveVO) {
        if (StringUtils.isBlank(lotterySaveVO.getId())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(lotterySaveVO.getName())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(lotterySaveVO.getPreId())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(lotterySaveVO.getCode())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(lotterySaveVO.getStatus())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }
        if (StringUtils.isBlank(lotterySaveVO.getSummary())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Empty_Param);
        }

        int count = lotteryService.count(new LambdaQueryWrapper<Lottery>()
                .eq(Lottery::getId, lotterySaveVO.getId())
                        .eq(Lottery::getName, lotterySaveVO.getName())
                        .eq(Lottery::getPreId, lotterySaveVO.getPreId())
                        .eq(Lottery::getCode, lotterySaveVO.getCode())
                        .eq(Lottery::getStatus, lotterySaveVO.getStatus())
                        .eq(Lottery::getSummary, lotterySaveVO.getSummary())
        );
        if (count > 0) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Exists);
        }

        Lottery newLottery = new Lottery();
        BeanUtils.copyProperties(lotterySaveVO, newLottery);

        lotteryService.save(newLottery);

        lotterySaveVO = new LotterySaveVO();
        BeanUtils.copyProperties(newLottery, lotterySaveVO);
        log.debug(JSON.toJSONString(lotterySaveVO));
        return lotterySaveVO;
    }


    /**
     * 根据条件code查询彩种一个详情信息
     *
     * @param code 编码
     * @return LotteryVO
     */
    @ApiOperation(value = "创建Lottery", notes = "创建Lottery")
    @GetMapping("/load/code/{code}")
    public LotteryVO loadByCode(@PathVariable java.lang.String code) {
        Lottery lottery = lotteryService.getOne(new LambdaQueryWrapper<Lottery>()
                .eq(Lottery::getCode, code));
        LotteryVO lotteryVO = new LotteryVO();
        BeanUtils.copyProperties(lottery, lotteryVO);
        log.debug(JSON.toJSONString(lotteryVO));
        return lotteryVO;
    }

    /**
     * 查询彩种信息集合
     *
     * @return 分页对象
     */
    @ApiOperation(value = "查询Lottery信息集合", notes = "查询Lottery信息集合")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "curPage", value = "当前页", required = true, paramType = "query"),
            @ApiImplicitParam(name = "pageSize", value = "分页大小", required = true, paramType = "query"),
    })
    @GetMapping(value = "/list")
    public IPage<LotteryPageVO> list(@ApiIgnore LotteryPageVO lotteryVO, Integer curPage, Integer pageSize) {
        IPage<Lottery> page = new Page<>(curPage, pageSize);
        QueryWrapper<Lottery> queryWrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(lotteryVO.getPreId())) {
            queryWrapper.lambda().eq(Lottery::getPreId, lotteryVO.getPreId());
        }
        if (StringUtils.isNotBlank(lotteryVO.getStatus())) {
            queryWrapper.lambda().eq(Lottery::getStatus, lotteryVO.getStatus());
        }
        int total = lotteryService.count(queryWrapper);
        if (total > 0) {
            queryWrapper.lambda().orderByDesc(Lottery::getId);

            IPage<Lottery> lotteryPage = lotteryService.page(page,queryWrapper);
            List<LotteryPageVO> lotteryPageVOList = JSON.parseArray(JSON.toJSONString(lotteryPage.getRecords()), LotteryPageVO.class);
            IPage<LotteryPageVO> iPage = new Page<>();
            iPage.setPages(lotteryPage.getPages());
            iPage.setCurrent(curPage);
            iPage.setSize(pageSize);
            iPage.setTotal(lotteryPage.getTotal());
            iPage.setRecords(lotteryPageVOList);
            log.debug(JSON.toJSONString(iPage));
            return iPage;
        }
        return new Page<>();
    }


    /**
     * 修改 彩种
     *
     * @return R
     */
    @ApiOperation(value = "修改Lottery", notes = "修改Lottery")
    @PutMapping("/modify")
    public boolean modify(@ApiParam(name = "修改Lottery", value = "传入json格式", required = true)
                          @RequestBody LotteryVO lotteryVO) {
        if (StringUtils.isBlank(lotteryVO.getId())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Lottery newLottery = new Lottery();
        BeanUtils.copyProperties(lotteryVO, newLottery);
        boolean isUpdated = lotteryService.update(newLottery, new LambdaQueryWrapper<Lottery>()
                .eq(Lottery::getId, lotteryVO.getId()));
        return isUpdated;
    }


    /**
     * 删除 彩种
     *
     * @return R
     */
    @ApiOperation(value = "删除Lottery", notes = "删除Lottery")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "id", paramType = "query"),
            @ApiImplicitParam(name = "code", value = "编码", paramType = "query")
    })
    @DeleteMapping("/delete")
    public R delete(@ApiIgnore LotteryVO lotteryVO) {
        if (StringUtils.isBlank(lotteryVO.getId())) {
            throw new LotteryException(BaseException.BaseExceptionEnum.Ilegal_Param);
        }
        Lottery newLottery = new Lottery();
        BeanUtils.copyProperties(lotteryVO, newLottery);
        lotteryService.remove(new LambdaQueryWrapper<Lottery>()
                .eq(Lottery::getId, lotteryVO.getId()));
        return R.success("删除成功");
    }

}
