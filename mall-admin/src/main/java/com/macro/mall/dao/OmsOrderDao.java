package com.macro.mall.dao;

import com.macro.mall.dto.OmsOrderDeliveryParam;
import com.macro.mall.dto.OmsOrderDetail;
import com.macro.mall.dto.OmsOrderQueryParam;
import com.macro.mall.model.OmsOrder;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 订单查询自定义Dao
 */
public interface OmsOrderDao {

    List<OmsOrder> getList(@Param("queryParam") OmsOrderQueryParam queryParam);

    int delivery(@Param("list") List<OmsOrderDeliveryParam> deliveryParamList);

    OmsOrderDetail getDetail(@Param("id") Long id);
}
