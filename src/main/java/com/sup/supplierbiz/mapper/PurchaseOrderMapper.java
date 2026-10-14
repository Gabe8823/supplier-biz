package com.sup.supplierbiz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sup.supplierbiz.common.enums.OrderStatus;
import com.sup.supplierbiz.domain.po.PurchaseOrder;
import com.sup.supplierbiz.domain.vo.PurchaseOrderVO;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;

/**
 * 采购订单 Mapper
 *
 * @author Gabe
 * @date 2026-10-09
 */
public interface PurchaseOrderMapper extends BaseMapper<PurchaseOrder> {

    /**
     * 分页mapper
     * @param page
     * @param supplierName
     * @param status
     * @param startDate
     * @param endDate
     * @return
     */
    Page<PurchaseOrderVO> selectPageVO(Page<PurchaseOrderVO> page,
                                       @Param("supplierName") String supplierName,
                                       @Param("status") OrderStatus status,
                                       @Param("startDate") LocalDate startDate,
                                       @Param("endDate") LocalDate endDate);
}
