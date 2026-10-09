package com.sup.supplierbiz.domain.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.sup.supplierbiz.common.enums.Deleted;
import com.sup.supplierbiz.common.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 系统用户持久化实体
 *
 * @author sup
 * @date 2026-10-09
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_user")
public class User {
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private Status status;
    @TableLogic
    /** 删除 1/0 */ private Deleted deleted;
    /** 创建时间 */ private LocalDateTime createdAt;
    /** 更新时间 */ private LocalDateTime updatedAt;
}
