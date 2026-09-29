package com.sup.supplierbiz.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    @Parameter(description = "总条数")
    private long total;
    @Parameter(description = "当前页记录数")
    private List<T> records;
    @Parameter(description = "页码")
    private long pageNum;
    @Parameter(description = "每页条数")
    private long pageSize;
    public static <T> PageDTO<T> of(Page<T> page){
        return new PageDTO<>(page.getTotal(),page.getRecords(), page.getCurrent(), page.getSize());
    }
}
