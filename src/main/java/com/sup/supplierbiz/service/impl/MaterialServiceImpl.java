package com.sup.supplierbiz.service.impl;

import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sup.supplierbiz.domain.po.Materials;
import com.sup.supplierbiz.mapper.MaterialsMapper;
import com.sup.supplierbiz.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * 物料服务实现
 *
 * @author sup
 * @date 2026-10-09
 */
@Service
@RequiredArgsConstructor
public class MaterialServiceImpl extends ServiceImpl<MaterialsMapper, Materials> implements MaterialService{
    private final StringRedisTemplate redis;

    /** 缓存 key 前缀 */
    private static final String CACHE_KEY_PREFIX = "material:detail:";
    /** 空值标记 */
    private static final String NULL_CACHE = "NULL";
    /** 基础TTL */
    private static final int BASE_TTL_MINUTES = 30;
    /** 空值TTL */
    private static final int NULL_TTL_MINUTES = 2;

    @Override
    public Materials getDetailById(Long id) {
        String key = CACHE_KEY_PREFIX + id;
        //1.查缓存
        String cached = redis.opsForValue().get(key);
        //2.缓存命中
        if(cached!=null){
            if (NULL_CACHE.equals(cached)){
                return null;
            }
            return JSONUtil.toBean(cached, Materials.class);
        }
        //3.缓存未命中
        Materials materials = getById(id);
        if (materials==null){
            //4.库中无数据，缓存空值
            redis.opsForValue().set(key,NULL_CACHE,NULL_TTL_MINUTES, TimeUnit.MINUTES);
            return null;
        }
        //5.库中有值，缓存回填
        int ttl = BASE_TTL_MINUTES + ThreadLocalRandom.current().nextInt(-5, 6);
        redis.opsForValue().set(key,JSONUtil.toJsonStr(materials),ttl,TimeUnit.MINUTES);
        return materials;
    }
}
