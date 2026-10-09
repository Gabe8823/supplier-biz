package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.po.Materials;
import org.springframework.stereotype.Service;


/**
 * 物料服务接口
 *
 * @author sup
 * @date 2026-10-09
 */
public interface MaterialService extends IService<Materials> {
    /**
     * 物料详情查询
     * @param id
     * @return
     */
    Materials getDetailById(Long id);
}
