package com.movision.mybatis.period.service;

import com.movision.mybatis.period.entity.Period;
import com.movision.mybatis.period.mapper.PeriodMapper;
import com.movision.mybatis.post.entity.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @Author zhurui
 * @Date 2017/2/8 14:36
 */
@Service
@Transactional
public class PeriodService {
    Logger log = LoggerFactory.getLogger(PeriodService.class);
    @Autowired
    PeriodMapper periodMapper;

    /**
     * 添加活动周期
     *
     * @param map
     * @return
     */
    public int insertPostRecord(Period map) {
        try {
            log.info("添加活动周期");
            return periodMapper.insertSelectiveTwo(map);
        } catch (Exception e) {
            log.error("活动周期添加失败");
            throw e;
        }
    }

    /**
     * 查询活动周期
     * @param postid
     * @return
     */
    public Period queryPostPeriod(int postid){
        try{
           log.info("查询活动周期");
            return periodMapper.findAllPeriod(postid);
        }catch(Exception e){
            log.error("查询活动周期失败");
            throw e;
        }
    }

    /**
     * 添加活动周期
     *
     * @param period
     * @return
     */
    public Integer insertActivePostPer(Period period) {
        try {
            log.info("添加活动周期");
            return periodMapper.insertSelective(period);
        } catch (Exception e) {
            log.error("添加活动周期异常", e);
            throw e;
        }
    }

    /**
     * 删除活动周期
     *
     * @param period
     */
    public void deleteActiveePostPer(Period period) {
        try {
            log.info("删除活动周期");
            periodMapper.deleteActiveePostPer(period);
        } catch (Exception e) {
            log.error("删除活动周期异常", e);
            throw e;
        }
    }
}
