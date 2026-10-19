package com.sup.supplierbiz.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.common.enums.ResultCode;
import com.sup.supplierbiz.common.enums.Status;
import com.sup.supplierbiz.common.exception.BusinessException;
import com.sup.supplierbiz.domain.dto.PageDTO;
import com.sup.supplierbiz.domain.dto.PageQuery;
import com.sup.supplierbiz.domain.dto.SuppliersCreateDTO;
import com.sup.supplierbiz.domain.dto.SuppliersUpdateDTO;
import com.sup.supplierbiz.domain.po.Suppliers;
import com.sup.supplierbiz.domain.vo.SuppliersVO;
import com.sup.supplierbiz.mapper.SuppliersMapper;
import com.sup.supplierbiz.service.SuppliersService;
import org.springframework.stereotype.Service;

/**
 * @author Gabe
 * @description 针对表【suppliers(供应商表)】的数据库操作Service实现
 * @createDate 2026-10-01 15:49:32
 */
@Service
public class SuppliersServiceImpl extends ServiceImpl<SuppliersMapper, Suppliers>
        implements SuppliersService {

    @Override
    public PageDTO<SuppliersVO> pageSuppliers(PageQuery query, String supplierName) {
        Page<Suppliers> pagePO = lambdaQuery()
                .like(StrUtil.isNotBlank(supplierName), Suppliers::getSupplierName, supplierName)
                .orderByDesc(Suppliers::getId)
                .page(Page.of(query.getPageNum(), query.getPageSize()));
        IPage<SuppliersVO> pageVO = pagePO.convert(po -> BeanUtil.copyProperties(po, SuppliersVO.class));
        return PageDTO.of(pageVO);
    }

    @Override
    public SuppliersVO findById(Long id) {
        Suppliers suppliers = getById(id);
        if (suppliers == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        return BeanUtil.copyProperties(suppliers, SuppliersVO.class);
    }

    @Override
    public Long addSupplier(SuppliersCreateDTO createDTO) {
        Suppliers suppliers = new Suppliers();
        Long count = lambdaQuery().eq(Suppliers::getSupplierCode, createDTO.getSupplierCode()).count();
        if (count > 0) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        BeanUtil.copyProperties(createDTO, suppliers);
        suppliers.setStatus(Status.ENABLE);
        save(suppliers);
        return suppliers.getId();
    }

    @Override
    public Void updateSupplier(SuppliersUpdateDTO updateDTO) {

        Long id = updateDTO.getId();
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        if (getById(id) == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        lambdaUpdate()
                .eq(Suppliers::getId, id)
                .set(Suppliers::getSupplierName, updateDTO.getSupplierName())
                .set(Suppliers::getShortName, updateDTO.getShortName())
                .set(Suppliers::getContactPerson, updateDTO.getContactPerson())
                .set(Suppliers::getContactPhone, updateDTO.getContactPhone())
                .set(Suppliers::getEmail, updateDTO.getEmail())
                .set(Suppliers::getAddress, updateDTO.getAddress())
                .set(Suppliers::getTaxId, updateDTO.getTaxId())
                .set(Suppliers::getBankInfo, updateDTO.getBankInfo())
                .set(Suppliers::getSupplierType, updateDTO.getSupplierType())
                .update();

        return null;
    }

    @Override
    public Void deleteById(Long id) {
        Suppliers suppliers = getById(id);
        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        if (suppliers == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        removeById(id);
        return null;
    }

    @Override
    public Void updateStatus(Long id, Status status) {

        if (id == null) {
            throw new BusinessException(ResultCode.PARAM_ERROR);
        }
        Suppliers suppliers = getById(id);
        if (suppliers == null) {
            throw new BusinessException(ResultCode.NOT_FOUND);
        }
        suppliers.setStatus(status);
        updateById(suppliers);
        return null;
    }
}




