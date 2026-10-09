package com.sup.supplierbiz.controller;

import com.sup.supplierbiz.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 健康检查接口
 *
 * @author sup
 * @date 2026-10-09
 */
@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    public Result<String> result(){
        return Result.success("ok");
    }
}
