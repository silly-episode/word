package com.boot.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/1/31 9:28
 * @FileName: RedisUtils
 * @Description: redis工具类
 */
@Component
@Slf4j
public class RedisUtils {

    private final String DEFAULT_KEY_PREFIX = "word";
    private final int EXPIRE_TIME = 1;
    private final TimeUnit EXPIRE_TIME_TYPE = TimeUnit.DAYS;
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * @param key:
     * @param value:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 添加key-value
     * @Date: 2023/2/6 19:35
     */
    public <K, V> void add(K key, V value) {
        try {
            if (value != null) {
                redisTemplate
                        .opsForValue()
                        .set(DEFAULT_KEY_PREFIX + key, value);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("数据缓存至redis失败");
        }
    }

    /**
     * @param key:
     * @param value:
     * @param timeout:
     * @param unit:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 数据缓存至redis并设置过期时间
     * @Date: 2023/2/6 19:35
     */
    public <K, V> void add(K key, V value, long timeout, TimeUnit unit) {
        try {
            if (value != null) {
                redisTemplate
                        .opsForValue()
                        .set(DEFAULT_KEY_PREFIX + key, value, timeout, unit);
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("数据缓存至redis失败");
        }
    }

    /**
     * @param key:
     * @param subKey:
     * @param value:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 写入 hash-set,已经是key-value的键值，不能再写入为hash-set
     * @Date: 2023/2/6 19:36
     */
    public <K, SK, V> void addHashCache(K key, SK subKey, V value) {
        redisTemplate.opsForHash().put(DEFAULT_KEY_PREFIX + key, subKey, value);
    }

    /**
     * @param key:
     * @param subKey:
     * @param value:
     * @param timeout:
     * @param unit:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 写入 hash-set,并设置过期时间
     * @Date: 2023/2/6 19:36
     */
    public <K, SK, V> void addHashCache(K key, SK subKey, V value, long timeout, TimeUnit unit) {
        redisTemplate.opsForHash().put(DEFAULT_KEY_PREFIX + key, subKey, value);
        redisTemplate.expire(DEFAULT_KEY_PREFIX + key, timeout, unit);
    }

    /**
     * @param key:
     * @param subKey:
     * @Return: Object
     * @Author: DengYinzhe
     * @Description: 获取 hash-set-value
     * @Date: 2023/2/6 19:37
     */
    public <K, SK> Object getHashCache(K key, SK subKey) {
        return redisTemplate.opsForHash().get(DEFAULT_KEY_PREFIX + key, subKey);
    }


    /**
     * @param key:
     * @param clazz:
     * @Return: V
     * @Author: DengYinzhe
     * @Description: 从redis中获取缓存数据，转成对象
     * @Date: 2023/2/6 19:38
     */
    public <K, V> V getObject(K key, Class<V> clazz) {
        String value = this.get(key);
        V result = null;
        if (!StringUtils.isEmpty(value)) {
            result = JsonUtils.getJsonToBean(value, clazz);
        }
        return result;
    }

    /**
     * @param key:
     * @param clazz:
     * @Return: List<V>
     * @Author: DengYinzhe
     * @Description: 从redis中获取缓存数据，转成list
     * @Date: 2023/2/6 19:39
     */
    public <K, V> List<V> getList(K key, Class<V> clazz) {
        String value = this.get(key);
        List<V> result = Collections.emptyList();
        if (!StringUtils.hasLength(value)) {
            result = JsonUtils.getJsonToList(value, clazz);
        }
        return result;
    }

    /**
     * @param key:
     * @Return: String
     * @Author: DengYinzhe
     * @Description: 根据key获取value
     * @Date: 2023/2/6 19:39
     */
    public <K> String get(K key) {
        String value;
        try {
            value = (String) redisTemplate.opsForValue().get(DEFAULT_KEY_PREFIX + key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("从redis缓存中获取缓存数据失败");
        }
        return value;
    }

    /**
     * @param key:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 删除key
     * @Date: 2023/2/6 19:39
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * @param keys:
     * @Return: void
     * @Author: DengYinzhe
     * @Description: 批量删除key
     * @Date: 2023/2/6 19:40
     */
    public void delete(Collection<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * @param key:
     * @Return: byte
     * @Author: DengYinzhe
     * @Description: 序列化key
     * @Date: 2023/2/6 19:40
     */
    public byte[] dump(String key) {
        return redisTemplate.dump(key);
    }

    /**
     * @param key:
     * @Return: Boolean
     * @Author: DengYinzhe
     * @Description: 是否存在key
     * @Date: 2023/2/6 19:40
     */
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * @param key:
     * @param timeout:
     * @param unit:
     * @Return: Boolean
     * @Author: DengYinzhe
     * @Description: 设置过期时间
     * @Date: 2023/2/6 19:40
     */
    public Boolean expire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    /**
     * @param key:
     * @param date:
     * @Return: Boolean
     * @Author: DengYinzhe
     * @Description: 设置过期时间
     * @Date: 2023/2/6 19:41
     */
    public Boolean expireAt(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }


    /**
     * @param key:
     * @Return: Boolean
     * @Author: DengYinzhe
     * @Description: 移除 key 的过期时间，key 将持久保持
     * @Date: 2023/2/6 19:41
     */
    public Boolean persist(String key) {
        return redisTemplate.persist(key);
    }

    /**
     * @param key:
     * @param unit:
     * @Return: Long
     * @Author: DengYinzhe
     * @Description: 返回 key 的剩余的过期时间
     * @Date: 2023/2/6 19:41
     */
    public Long getExpire(String key, TimeUnit unit) {
        return redisTemplate.getExpire(key, unit);
    }

    /**
     * @param key:
     * @Return: Long
     * @Author: DengYinzhe
     * @Description: 返回 key 的剩余的过期时间
     * @Date: 2023/2/6 19:41
     */
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }
}
