package com.sup.supplierbiz.domain.dto;

import lombok.Data;

@Data
public class PageQuery {
    private  long pageNum = 1;
    private long pageSize = 10;
}
