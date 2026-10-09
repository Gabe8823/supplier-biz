package com.sup.supplierbiz.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.sup.supplierbiz.domain.dto.LoginDTO;
import com.sup.supplierbiz.domain.po.User;

/**
 * 用户服务接口
 *
 * @author sup
 * @date 2026-10-09
 */
public interface UserService extends IService<User> {

    /**
     * 用户登录
     * @param dto
     * @return
     */
    String login(LoginDTO dto);
}
