package com.sup.supplierbiz.service.impl;

import cn.hutool.crypto.digest.BCrypt;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.common.enums.ResultCode;
import com.sup.supplierbiz.common.enums.Status;
import com.sup.supplierbiz.common.exception.BusinessException;
import com.sup.supplierbiz.domain.dto.LoginDTO;
import com.sup.supplierbiz.domain.po.User;
import com.sup.supplierbiz.mapper.UserMapper;
import com.sup.supplierbiz.service.UserService;
import com.sup.supplierbiz.util.JWTUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final JWTUtils jwtUtils;

    @Override
    public String login(LoginDTO dto) {
        User user = lambdaQuery().eq(User::getUsername,dto.getUsername()).one();
        if (user==null){
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        if (user.getStatus()==Status.DISABLE){
            throw new BusinessException(ResultCode.ACCOUNT_DISABLED);
        }
        if (!BCrypt.checkpw(dto.getPassword(),user.getPassword())){
            throw new BusinessException(ResultCode.LOGIN_FAILED);
        }
        return jwtUtils.generateToken(user.getId(), user.getUsername());
    }
}
