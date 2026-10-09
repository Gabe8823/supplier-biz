package com.sup.supplierbiz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.domain.po.Suppliers;
import com.sup.supplierbiz.service.SuppliersService;
import com.sup.supplierbiz.mapper.SuppliersMapper;
import org.springframework.stereotype.Service;

/**
* @author sup
* @description 针对表【suppliers(供应商表)】的数据库操作Service实现
* @createDate 2026-10-01 15:49:32
*/
@Service
public class SuppliersServiceImpl extends ServiceImpl<SuppliersMapper, Suppliers>
    implements SuppliersService{

}




