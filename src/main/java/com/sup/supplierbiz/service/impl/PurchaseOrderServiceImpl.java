package com.sup.supplierbiz.service.impl;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.common.enums.OrderStatus;
import com.sup.supplierbiz.common.enums.ResultCode;
import com.sup.supplierbiz.common.exception.BusinessException;
import com.sup.supplierbiz.domain.dto.Items;
import com.sup.supplierbiz.domain.dto.PurchaseOrderCreateDTO;
import com.sup.supplierbiz.domain.po.Materials;
import com.sup.supplierbiz.domain.po.PurchaseOrder;
import com.sup.supplierbiz.domain.po.PurchaseOrderItem;
import com.sup.supplierbiz.mapper.PurchaseOrderMapper;
import com.sup.supplierbiz.service.MaterialService;
import com.sup.supplierbiz.service.PurchaseOrderItemService;
import com.sup.supplierbiz.service.PurchaseOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl extends ServiceImpl<PurchaseOrderMapper, PurchaseOrder> implements PurchaseOrderService {
    private final MaterialService materialService;
    private final PurchaseOrderItemService purchaseOrderItemService;
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Long createOrder(PurchaseOrderCreateDTO dto,Long userId) {
        //检查items非空
        List<Items> items = dto.getItems();
        if(items==null||items.isEmpty()){
            throw new BusinessException(ResultCode.PARAM_ERROR,"采购明细不能为空");
    }
        //构建PurchaseOrder
        PurchaseOrder order = new PurchaseOrder();
        BeanUtil.copyProperties(dto,order);
        order.setStatus(OrderStatus.PENDING); // 服务端定
        order.setCreatedBy(userId);
        order.setUpdatedBy(userId);
        order.setPoNo(generatePoNo());

        //保存
        save(order);
        Long id = order.getId();
        Set<Long> materialIds = items.stream().map(Items::getMaterialId).collect(Collectors.toSet());
        Map<Long, Materials> materialsMap = materialService.listByIds(materialIds).stream().collect(Collectors.toMap(Materials::getId, m -> m));

        BigDecimal totalAmount = BigDecimal.ZERO;         // 不含税总额，先放 0
        BigDecimal totalTax = BigDecimal.ZERO;            // 税额合计，先放 0
        BigDecimal totalAmountWithTax = BigDecimal.ZERO;  // 价税合计，先放 0
        List<PurchaseOrderItem> itemList = new ArrayList<>();
        int sort = 1;
        for (Items item : items) {
            Materials materials = materialsMap.get(item.getMaterialId());
            if (materials==null){
                throw new BusinessException(ResultCode.PARAM_ERROR,"物料不存在"+item.getMaterialId());
            }
            BigDecimal amount = item.getOrderQuantity().multiply(item.getUnitPrice());
            BigDecimal taxAmount = amount.multiply(item.getTaxRate()).divide( new BigDecimal ( "100" ), 2 , RoundingMode.HALF_UP);
            BigDecimal amountWithTax = amount.add(taxAmount);

            totalAmount = totalAmount.add(amount);
            totalTax = totalTax.add(taxAmount);
            totalAmountWithTax = totalAmountWithTax.add(amountWithTax);

            PurchaseOrderItem poi = new PurchaseOrderItem();

            // 来自"主表"：明细必须知道自己是哪张单的
            poi.setPoId(id);

            // 来自"前端传的 item"：这条明细买了什么、买多少、什么价
            poi.setMaterialId(item.getMaterialId());
            poi.setOrderQuantity(item.getOrderQuantity());
            poi.setUnitPrice(item.getUnitPrice());
            poi.setTaxRate(item.getTaxRate());

            // 来自"物料主数据"：规格和单位以库里为准，不信前端传的
            poi.setSpecification(materials.getSpecification());
            poi.setUnit(materials.getUnit());

            // 来自"上面算出来的三个金额"
            poi.setAmount(amount);
            poi.setTaxAmount(taxAmount);
            poi.setAmountWithTax(amountWithTax);

            // 行号：第1行=1，第2行=2……
            poi.setSortOrder(sort++);

            // 放进筐里，循环结束后一起批量插库
            itemList.add(poi);
        }
        // 6. 回填主表三个总额
        order.setTotalAmount(totalAmount);
        order.setTotalTax(totalTax);
        order.setTotalAmountWithTax(totalAmountWithTax);
        updateById(order);
        // 7. 批量插入明细
        purchaseOrderItemService.saveBatch(itemList);
        return id;
    }

    private String generatePoNo() {
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int rand = new Random ().nextInt( 9000 ) + 1000 ;
        return "PO" + date + rand;
    }
}
