package com.sup.supplierbiz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.sup.supplierbiz.domain.po.Inventory;
import com.sup.supplierbiz.domain.vo.InventoryVO;
import org.apache.ibatis.annotations.Param;

/**
* @author 23219
* @description 针对表【inventory(库存表)】的数据库操作Mapper
* @createDate 2026-10-01 15:03:02
* @Entity com.sup.supplierbiz.domain.po.Inventory
*/
public interface InventoryMapper extends BaseMapper<Inventory> {

    /**
     * 分页查询库存（联表物料/仓库名称）
     *
     * @param page 分页对象，查询结果会回填到此对象
     * @param materialName 物料名称，模糊匹配，可为空
     * @return 分页结果，与传入的 page 为同一对象
     */
    Page<InventoryVO> selectPageVO(Page<InventoryVO> page, @Param("materialName") String materialName);
}




