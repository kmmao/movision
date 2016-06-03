package com.zhuhuibao.mybatis.tech.service;/**
 * @author Administrator
 * @version 2016/5/31 0031
 */

import com.zhuhuibao.mybatis.tech.entity.TechData;
import com.zhuhuibao.mybatis.tech.mapper.TechDataMapper;
import com.zhuhuibao.utils.pagination.model.Paging;
import com.zhuhuibao.utils.pagination.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *技术资料业务处理类
 *@author pl
 *@create 2016/5/31 0031
 **/
@Service
@Transactional
@RequestMapping(value="")
public class TechDataService {

    private final static Logger log = LoggerFactory.getLogger(TechDataService.class);

    @Autowired
    TechDataMapper techDataMapper;

    /**
     * 运营管理平台技术资料搜索
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String,String>> findAllOMSTechCooperationPager(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all OMS tech data for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> techList = null;
        try{
            techList = techDataMapper.findAllOMSTechDataPager(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all OMS tech data for pager error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 技术频道技术资料搜索
     * @param condition 搜索条件
     * @return
     */
    public List<Map<String,String>> findAllTechDataPager(Paging<Map<String,String>> pager, Map<String,Object> condition)
    {
        log.info("find all tech data for pager "+ StringUtils.mapToString(condition));
        List<Map<String,String>> techList = null;
        try{
            techList = techDataMapper.findAllTechDataPager(pager.getRowBounds(),condition);
        }catch(Exception e)
        {
            log.error("find all tech data for pager error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 插入技术资料：行业解决方案，技术文档，培训资料
     * @param techData
     * @return
     */
    public int insertTechData(TechData techData)
    {
        int result = 0;
        log.info("insert tech data info "+ StringUtils.beanToString(techData));
        try {
            result = techDataMapper.insertSelective(techData);
        }catch(Exception e)
        {
            log.error("insert data cooperation info error!",e);
            throw e;
        }
        return result;
    }

    /**
     * 注销技术资料
     * @param condition
     * @return
     */
    public int deleteTechData(Map<String, Object> condition)
    {
        int result;
        log.info("delete oms tech data "+StringUtils.mapToString(condition));
        try{
            result = techDataMapper.deleteByPrimaryKey(condition);
        }catch (Exception e){
            log.error("delete oms tech data error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 更新技术资料
     * @param techData
     * @return
     */
    public int updateTechData(TechData techData)
    {
        int result;
        log.info("update oms tech data "+StringUtils.beanToString(techData));
        try{
            result = techDataMapper.updateByPrimaryKeySelective(techData);
        }catch (Exception e){
            log.error("update oms tech data error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 更新点击率或者下载率
     * @param map
     * @return
     */
    public int updateTechDataViewsOrDL(Map<String,Object> map)
    {
        int result;
        log.info("update tech data views or download rate "+StringUtils.mapToString(map));
        try{
            result = techDataMapper.updateTechDataViewsOrDL(map);
        }catch (Exception e){
            log.error("update tech data views or download rate error! ",e);
            throw e;
        }
        return result;
    }

    /**
     * 查询技术资料信息
     * @param id 技术资料ID
     * @return
     */
    public TechData selectTechDataInfo(Long id)
    {
        TechData techData;
        log.info("select oms tech data "+id);
        try{
            techData = techDataMapper.selectByPrimaryKey(id);
        }catch (Exception e){
            log.error("select oms tech data error! ",e);
            throw e;
        }
        return techData;
    }

    /**
     * 获取技术资料文件名称
     * @param id 技术资料ID
     * @return
     */
    public String selectTechDataAttachName(Long id)
    {
        String fileName = null;
        log.info("select tech data attach file name "+id);
        try{
            TechData techData = techDataMapper.selectByPrimaryKey(id);
            if(techData != null)
            {
                fileName = techData.getAttach();
            }
        }catch (Exception e){
            log.error("select tech data attach file name error! ",e);
            throw e;
        }
        return fileName;
    }

    /**
     * 预览技术资料详情
     * @param id 技术资料ID
     * @return
     */
    public List<Map<String,String>> previewTechDataDetail(Long id)
    {
        log.info("preview tech data detail id = "+id);
        List<Map<String,String>> techList = null;
        try{
            techList = techDataMapper.previewTechDataDetail(id);
        }catch(Exception e)
        {
            log.error("preview tech data detail error!",e);
            throw e;
        }
        return techList;
    }

    /**
     * 查询解决方案，技术资料，培训资料的点击排行
     * @param map
     * @return
     */
    public List<Map<String,String>> findDataViewsOrder(Map<String,Object> map)
    {
        log.info("find views order "+StringUtils.mapToString(map));
        List<Map<String,String>> dataList = null;
        try{
            dataList = techDataMapper.findViewsOrder(map);
        }catch(Exception e)
        {
            log.error("find views order error!",e);
            throw e;
        }
        return dataList;
    }

    /**
     * 查询解决方案，技术资料，培训资料的下载排行
     * @param map
     * @return
     */
    public List<Map<String,String>> findDownloadOrder(Map<String,Object> map)
    {
        log.info("find views order "+StringUtils.mapToString(map));
        List<Map<String,String>> dataList = null;
        try{
            dataList = techDataMapper.findDownloadOrder(map);
        }catch(Exception e)
        {
            log.error("find views order error!",e);
            throw e;
        }
        return dataList;
    }

    /**
     * 查询技术频道首页的解决方案，技术资料，培训资料
     * @param map
     * @return
     */
    public Map<String,List<Map<String,String>>> findIndexTechData(Map<String,Object> map)
    {
        log.info("find home page tech data "+StringUtils.mapToString(map));
        Map<String,List<Map<String,String>>> techMap = new HashMap<String,List<Map<String,String>>>();
        try{
            List<Map<String,String>> dataList = techDataMapper.findIndexTechData(map);
            int size= dataList.size();
            List<Map<String,String>> techDataList;
            for(int i = 0;i < size;i++)
            {
                Map<String,String> tData = dataList.get(i);
                //二级分类
                String sCategory = String.valueOf(tData.get("sCategory"));
                if(techMap.get(sCategory) != null)
                {
                    techDataList = techMap.get(sCategory);
                    techDataList.add(tData);
                }
                else
                {
                    techDataList = new ArrayList<Map<String,String>>();
                    techDataList.add(tData);
                    techMap.put(sCategory,techDataList);
                }
            }
        }catch(Exception e)
        {
            log.error("find home page tech data error!",e);
            throw e;
        }
        return techMap;
    }
}
