package com.boot.utils;


import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/2/2 20:06
 * @FileName: BeanDtoVoUtils
 * @Description: DTO和Entity转换
 */
public class BeanDtoVoUtils {

    /**
     * TODO  dot ,Do ,entity 相互转换
     * 同：BeanUtils.copyProperties(dtoEntity, newInstance);
     *
     * @param oldClass 原数据--Dto，Vo，entity
     * @param newClass 转换为--Dto，Vo，entity
     */
    public static <E> E convert(Object oldClass, Class<E> newClass) {
        // 判断oldClass 是否为空!
        if (oldClass == null) {
            return null;
        }
        // 判断newClass 是否为空
        if (newClass == null) {
            return null;
        }
        try {
            // 创建新的对象实例
            E newInstance = newClass.getDeclaredConstructor().newInstance();

            // 把原对象数据拷贝到新的对象
            BeanUtils.copyProperties(oldClass, newInstance);
            // 返回新对象
            return newInstance;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * @param oldList:
     * @param v:
     * @Return: List<V>
     * @Author: DengYinzhe
     * @Description: list转换
     * @Date: 2023/3/27 13:04
     */
    public static <T, V> List<V> convertList(List<T> oldList, Class<V> v) {
        try {
            List<V> newList = new ArrayList<>();
            for (T t : oldList) {
                V temp = (V) BeanDtoVoUtils.convert(t, v.getDeclaredConstructor().newInstance().getClass());
                newList.add(temp);
            }
            return newList;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @param page:
     * @param v:
     * @Return: Page<V>
     * @Author: DengYinzhe
     * @Description: Page<Entity> 分页对象转 Page<Vo>  ( list 循环)
     * @Date: 2023/2/2 20:33
     */
    public static <T, V> Page<V> pageVo(Page<T> page, Class<V> v) {
        try {
            List<T> tList = page.getContent();
            List<V> voList = new ArrayList<>();
            for (T t : tList) {
                V temp = (V) BeanDtoVoUtils.convert(t, v.getDeclaredConstructor().newInstance().getClass());
                voList.add(temp);
            }
            return new PageImpl<>(voList, page.getPageable(), page.getTotalElements());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param page:
     * @param v:
     * @Return: Page<V>
     * @Author: DengYinzhe
     * @Description: Page<Entity> 分页对象转 Page<Vo> （Stream 方式）
     * @Date: 2023/2/2 20:33
     */
    public static <T, V> Page<V> pageVoStream(Page<T> page, Class<V> v) {
        List<V> voList = page.getContent().stream().map(item -> {
            try {
                return (V) BeanDtoVoUtils.convert(item, v.getDeclaredConstructor().newInstance().getClass());
            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toList());
        return new PageImpl<>(voList, page.getPageable(), page.getTotalElements());
    }


}
