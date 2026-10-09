package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.dto.StockChangeDTO;
import com.sup.supplierbiz.domain.po.Inventory;
import com.sup.supplierbiz.domain.vo.InventoryVO;

/**
* @author 23219
* @description 针对表【inventory(库存表)】的数据库操作Service
* @createDate 2026-10-01 15:03:02
*/
public interface InventoryService extends IService<Inventory> {

    /**
     * 库存分页查询（联表物料/仓库名称）
     *
     * @param query 分页参数
     * @param materialName 物料名称，模糊匹配，可为空
     * @return 分页结果
     */
    PageDTO<InventoryVO> pageInventory(PageQuery query, String materialName);

    /**
     * 入库
     * @param dto
     * @return
     */
    Long stockIn(StockChangeDTO dto);

    /**
     * 出库
     * @param dto
     */
    void stockOut(StockChangeDTO dto);
}
