package com.sup.supplierbiz.domain.dto;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果 DTO
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO<T> {
    /** 总条数（包装类型可区分未赋值与赋值为 0） */
    @Parameter(description = "总条数")
    private Long total;
    @Parameter(description = "当前页记录数")
    private List<T> records;
    /** 页码（包装类型可区分未赋值与赋值为 0） */
    @Parameter(description = "页码")
    private Long pageNum;
    /** 每页条数（包装类型可区分未赋值与赋值为 0） */
    @Parameter(description = "每页条数")
    private Long pageSize;
    public static <T> PageDTO<T> of(Page<T> page){
        return new PageDTO<>(page.getTotal(),page.getRecords(), page.getCurrent(), page.getSize());
    }
}
