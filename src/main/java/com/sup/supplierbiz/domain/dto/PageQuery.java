package com.sup.supplierbiz.domain.dto;

import lombok.Data;

/**
 * 分页查询参数 DTO
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
public class PageQuery {
    private  long pageNum = 1;
    private long pageSize = 10;
}
