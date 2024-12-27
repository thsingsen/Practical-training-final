package com.macro.mall.service;

import com.macro.mall.model.PmsSkuStock;

import java.util.List;


public interface PmsSkuStockService {
    /**
     * 根据商品id和skuCode关键字模糊搜索
     */
    List<PmsSkuStock> getList(Long pid, String keyword);

    /**
     * 批量更新商品库存信息
     */
    int update(Long pid, List<PmsSkuStock> skuStockList);
}
