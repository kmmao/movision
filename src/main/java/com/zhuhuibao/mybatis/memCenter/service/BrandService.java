package com.zhuhuibao.mybatis.memCenter.service;

import com.zhuhuibao.common.BrandBean;
import com.zhuhuibao.common.BrandDetailBean;
import com.zhuhuibao.common.ResultBean;
import com.zhuhuibao.common.SuggestBrand;
import com.zhuhuibao.mybatis.memCenter.entity.Brand;
import com.zhuhuibao.mybatis.memCenter.mapper.BrandMapper;
import com.zhuhuibao.mybatis.product.entity.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 品牌业务处理
 * Created by cxx on 2016/3/23 0023.
 */
@Service
@Transactional
public class BrandService {
    private static final Logger log = LoggerFactory.getLogger(BrandService.class);

    @Autowired
    private BrandMapper brandMapper;

    /**
     * 根据会员id，状态status查询品牌
     */
    public List<Brand> searchBrandByStatus(Brand brand)
    {
        log.debug("根据会员id，状态status查询品牌");
        List<Brand> brands = brandMapper.searchBrandByStatus(brand);
        return brands;
    }

    /**
     * 查询拥有产品的品牌
     */
    public List<BrandBean> searchAll()
    {
        log.debug("查询拥有产品的品牌");
        List<BrandBean> brandList = brandMapper.searchAll();
        return brandList;
    }

    /**
     * 查询拥有产品的推荐品牌
     */
    public List<SuggestBrand> SuggestBrand()
    {
        log.debug("查询拥有产品的推荐品牌");
        List<SuggestBrand> brandList = brandMapper.SuggestBrand();
        return brandList;
    }

    /**
     * 查询二级系统下所有品牌
     */
    public List<ResultBean> findAllBrand(Product product)
    {
        log.debug("查询二级系统下所有品牌");
        List<ResultBean> brandList = brandMapper.findAllBrand(product);
        return brandList;
    }

    /**
     * 查询推荐品牌
     */
    public List<ResultBean> searchSuggestBrand()
    {
        log.debug("查询推荐品牌");
        List<ResultBean> brandList = brandMapper.searchSuggestBrand();
        return brandList;
    }

    /**
     * 查询品牌详情
     */
    public BrandDetailBean details(String id)
    {
        log.debug("查询品牌详情");
        BrandDetailBean brand = brandMapper.details(id);
        return brand;
    }
}
