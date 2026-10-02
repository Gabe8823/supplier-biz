package com.sup.supplierbiz.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.sup.supplierbiz.domain.po.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
