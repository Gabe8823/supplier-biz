package com.sup.supplierbiz.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.domain.po.Materials;
import com.sup.supplierbiz.mapper.MaterialsMapper;
import com.sup.supplierbiz.service.MaterialService;
import org.springframework.stereotype.Service;

/**
 * 物料服务实现
 *
 * @author sup
 * @date 2026-10-09
 */
@Service
public class MaterialServiceImpl extends ServiceImpl<MaterialsMapper, Materials> implements MaterialService{
}
