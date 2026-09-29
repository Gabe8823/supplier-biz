package com.sup.supplierbiz.controller;

import com.sup.supplierbiz.common.result.Result;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/health")
public class HealthController {
    @GetMapping
    public Result<String> result(){
        return Result.success("ok");
    }
}
