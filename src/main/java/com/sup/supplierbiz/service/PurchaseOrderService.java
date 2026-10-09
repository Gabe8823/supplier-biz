package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.dto.PurchaseOrderCreateDTO;
import com.sup.supplierbiz.domain.po.PurchaseOrder;
import com.sup.supplierbiz.domain.vo.PurchaseOrderDetailVO;

/**
 * 采购订单服务接口
 *
 * @author sup
 * @date 2026-10-09
 */
public interface PurchaseOrderService extends IService<PurchaseOrder> {
    /**
     * 创建采购订单，同时生成订单明细并计算总额。
     *
     * @param dto    订单创建参数，包含供应商、下单日期、付款条款及明细行
     * @param userId 当前操作用户 id，用于写入 createdBy / updatedBy
     * @return 新建订单的主键 id
     */
    Long createOrder(PurchaseOrderCreateDTO dto,Long userId);

    /**
     * 查询采购订单详情，包含订单主表信息及明细行列表。
     *
     * @param id 订单主键 id
     * @return 订单详情 VO，包含主表字段与明细行
     */
    PurchaseOrderDetailVO getDetail(Long id);
}
