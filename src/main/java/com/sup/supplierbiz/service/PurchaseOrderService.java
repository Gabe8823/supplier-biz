package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.dto.PurchaseOrderCreateDTO;
import com.sup.supplierbiz.domain.po.PurchaseOrder;

public interface PurchaseOrderService extends IService<PurchaseOrder> {
    /**
     * 创建订单
     * @param dto
     * @return
     */
    Long createOrder(PurchaseOrderCreateDTO dto,Long userId);
}
