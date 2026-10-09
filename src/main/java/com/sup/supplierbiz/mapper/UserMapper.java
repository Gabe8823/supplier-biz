package com.sup.supplierbiz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sup.supplierbiz.domain.po.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户 Mapper
 *
 * @author sup
 * @date 2026-10-09
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
