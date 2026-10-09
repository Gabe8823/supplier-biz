package com.sup.supplierbiz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.domain.po.PurchaseOrderItem;
import com.sup.supplierbiz.mapper.PurchaseOrderItemMapper;
import com.sup.supplierbiz.service.PurchaseOrderItemService;
import org.springframework.stereotype.Service;

/**
 * 采购订单明细服务实现
 *
 * @author sup
 * @date 2026-10-09
 */
@Service
public class PurchaseOrderItemServiceImpl extends ServiceImpl<PurchaseOrderItemMapper, PurchaseOrderItem> implements PurchaseOrderItemService {
}
