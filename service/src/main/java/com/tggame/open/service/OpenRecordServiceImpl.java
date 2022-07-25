/*
 * tg学习代码
 */
package com.tggame.open.service;

import com.baidu.fsg.uid.UidGenerator;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.tggame.open.dao.OpenRecordDAO;
import com.tggame.open.dao.mapper.OpenRecordMapper;
import com.tggame.open.entity.OpenRecord;
import com.tggame.open.entity.OpenRecordStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * 开奖记录
 *
 * @author tg
 */
@Slf4j
@Service
public class OpenRecordServiceImpl extends ServiceImpl<OpenRecordMapper, OpenRecord> implements OpenRecordService {

  @Autowired
  private OpenRecordDAO openRecordDAO;

}
