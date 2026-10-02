package com.sup.supplierbiz.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sup.supplierbiz.common.result.Result;
import com.sup.supplierbiz.domain.dto.LoginDTO;
import com.sup.supplierbiz.domain.po.User;
import com.sup.supplierbiz.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.baomidou.mybatisplus.core.toolkit.Wrappers.lambdaQuery;
@Tag(name = "用户登录模块")
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    @Operation(summary = "用户登录列表")
    @PostMapping("/login")
    public Result<String> Login(@RequestBody LoginDTO dto){
        log.info("login请求体：{}",dto);
        return Result.success(userService.login(dto));
    }

}
