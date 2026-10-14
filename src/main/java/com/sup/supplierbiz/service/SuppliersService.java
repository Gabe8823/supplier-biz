package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sup.supplierbiz.common.enums.Status;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.dto.SuppliersCreateDTO;
import com.sup.supplierbiz.domain.dto.SuppliersUpdateDTO;
import com.sup.supplierbiz.domain.po.Suppliers;
import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.vo.SuppliersVO;

/**
* @author Gabe
* @description 针对表【suppliers(供应商表)】的数据库操作Service
* @createDate 2026-10-01 15:49:32
*/
public interface SuppliersService extends IService<Suppliers> {

    /**
     * 供应商分页查询
     *
     * @param query
     * @param supplierName
     * @param
     * @return
     */
    PageDTO<SuppliersVO> pageSuppliers(PageQuery query, String supplierName);


    /**
     * 根据id查询供应商
     * @param id
     * @return
     */
    SuppliersVO findById(Long id);

    /**
     * 新增供应商
     * @param createDTO
     * @return
     */
    Long addSupplier(SuppliersCreateDTO createDTO);

    /**
     * 修改供应商信息
     * @param updateDTO
     * @return
     */
    Void updateSupplier(SuppliersUpdateDTO updateDTO);

    /**
     * 根据id删除供应商
     * @param id
     * @return
     */
    Void deleteById(Long id);

    /**
     * 修改供应商状态
     * @param id
     * @param status
     * @return
     */
    Void updateStatus(Long id, Status status);
}
