package com.boot.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Map;
import java.util.Set;


public class JsonUtils
{
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成JSON数据
     * @param data 对象
     * @return JSON数据
     */
    public static String getBeanToJson(Object data)
    {
        try
        {
            String result = MAPPER.writeValueAsString(data);
            return result;
        }
        catch (JsonProcessingException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成对象
     * @param jsonData JSON数据
     * @param beanType 对象类型
     * @return 对象
     */
    public static <T> T getJsonToBean(String jsonData, Class<T> beanType)
    {
        try
        {
            T result = MAPPER.readValue(jsonData, beanType);
            return result;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成列表
     * @param jsonData JSON数据
     * @param beanType 对象类型
     * @return 列表
     */
    public static <T> List<T> getJsonToList(String jsonData, Class<T> beanType)
    {
        try
        {
            JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
            List<T> resultList = MAPPER.readValue(jsonData, javaType);
            return resultList;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成Set集合
     * @param jsonData JSON数据
     * @param elementType 元素类型
     * @return Set集合
     */
    public static <E> Set<E> getJsonToSet(String jsonData, Class<E> elementType)
    {
        try
        {
            JavaType javaType = MAPPER.getTypeFactory().constructCollectionType(Set.class, elementType);
            Set<E> resultSet = MAPPER.readValue(jsonData, javaType);
            return resultSet;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将JSON数据转换成Map集合
     * @param jsonData JSON数据
     * @param keyType 键类型
     * @param valueType 值类型
     * @return Map集合
     */
    public static <K, V> Map<K, V> getJsonToMap(String jsonData, Class<K> keyType, Class<V> valueType)
    {
        try
        {
            JavaType javaType = MAPPER.getTypeFactory().constructMapType(Map.class, keyType, valueType);
            Map<K, V> resultMap = MAPPER.readValue(jsonData, javaType);
            return resultMap;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
}