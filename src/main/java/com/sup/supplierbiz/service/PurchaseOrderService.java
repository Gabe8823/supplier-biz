package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.dto.PurchaseOrderCreateDTO;
import com.sup.supplierbiz.domain.po.PurchaseOrder;
import com.sup.supplierbiz.domain.vo.PurchaseOrderDetailVO;

public interface PurchaseOrderService extends IService<PurchaseOrder> {
    /**
     * 创建订单
     * @param dto
     * @return
     */
    Long createOrder(PurchaseOrderCreateDTO dto,Long userId);

    /**
     * 查询订单详情
     * @param id
     * @return
     */
    PurchaseOrderDetailVO getDetail(Long id);
}
