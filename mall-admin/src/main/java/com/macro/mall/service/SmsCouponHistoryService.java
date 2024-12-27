package com.macro.mall.service;

import com.macro.mall.model.SmsCouponHistory;

import java.util.List;

public interface SmsCouponHistoryService {

    List<SmsCouponHistory> list(Long couponId, Integer useStatus, String orderSn, Integer pageSize, Integer pageNum);
}
