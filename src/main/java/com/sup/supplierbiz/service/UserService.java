package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.dto.LoginDTO;
import com.sup.supplierbiz.domain.po.User;

public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param dto
     * @return
     */
    String login(LoginDTO dto);
}
