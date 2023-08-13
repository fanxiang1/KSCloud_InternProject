package com.ksyun.trade.utils;

import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 */
@Component
public class RedisUtils {

    /**
     * redis操作模版
     */
    private final RedisTemplate redisTemplate;

    public RedisUtils(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /** key的相关操作*/
    /**
     * 删除key
     * @param key
     */
    public void delete(String key){
        redisTemplate.delete(key);
    }

    /**
     * 批量删除key
     * @param keys
     */
    public void delete(Collection<String> keys){
        redisTemplate.delete(keys);
    }

    /**
     * 判断是否存在key
     * @param key
     * @return
     */
    public Boolean hasKey(String key){
        return redisTemplate.hasKey(key);
    }

    /**
     * 设置过期时间
     * @param key
     * @param timeout
     * @param util
     * @return
     */
    public Boolean expire(String key, long timeout, TimeUnit util){
        return redisTemplate.expire(key,timeout,util);
    }

    /**
     * 设置过期日期
     * @param key
     * @param date
     * @return
     */
    public Boolean expireAt(String key, Date date){
        return redisTemplate.expireAt(key,date);
    }

    /**
     * 去除key的过期时间，key将持久保持
     * @param key
     * @return
     */
    public Boolean persist(String key){
        return redisTemplate.persist(key);
    }

    /**
     * 返回key的剩余过期时间
     * @param key
     * @param unit
     * @return
     */
    public Long getExpire(String key,TimeUnit unit){
        return redisTemplate.getExpire(key,unit);
    }

    /**
     * 修改key的名称
     * @param oldKey
     * @param newKey
     */
    public void rename(String oldKey,String newKey){
        redisTemplate.rename(oldKey,newKey);
    }

    /**
     * 将key从当前的数据库移动到dbIndex数据库中
     * @param key
     * @param dbIndex
     * @return
     */
    public Boolean move(String key,int dbIndex){
        return redisTemplate.move(key,dbIndex);
    }

    /**
     * 查找匹配正则表达式的key
     * @param pattern
     * @return
     */
    public Set<String> keys(String pattern){
        return redisTemplate.keys(pattern);
    }

    /**
     * 序列化key
     * @param key
     * @return 字节数组
     */
    public byte[] dump(String key){
        return redisTemplate.dump(key);
    }


    /**
     * 返回key所存储的值的类型
     * @param key
     * @return
     */
    public DataType type(String key){
        return redisTemplate.type(key);
    }


    /**--------------------------------- String的相关操作----------------------------------- */
    /**
     * 设置指定key的值
     *
     * @param key
     * @param value
     */
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /**
     * 批量添加
     *
     * @param maps
     */
    public void multiSet(Map<String, String> maps) {
        redisTemplate.opsForValue().multiSet(maps);
    }

    /**
     * 获得指定key的值
     *
     * @param key
     * @return
     */
    public String get(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    /**
     * 批量获取keys的值
     *
     * @param keys
     * @return
     */
    public List<String> multiGet(Collection<String> keys) {
        return redisTemplate.opsForValue().multiGet(keys);
    }

    /**
     * 将给定的key的值设置为value，并返回key的旧的value值
     *
     * @param key
     * @param value
     * @return
     */
    public String getAndSet(String key, String value) {
        return (String) redisTemplate.opsForValue().getAndSet(key, value);
    }

    /**
     * 追加到末尾
     *
     * @param key
     * @param value
     * @return
     */
    public Integer append(String key, String value) {
        return redisTemplate.opsForValue().append(key, value);
    }


    /**--------------------------------- hash的相关操作----------------------------------- */
    /**
     * 设置key的field值
     *
     * @param key
     * @param field
     * @param value
     */
    public void hPut(String key, String field, String value) {
        redisTemplate.opsForHash().put(key, field, value);

    }

    /**
     * 批量设置key的field值
     *
     * @param key
     * @param maps
     */
    public void hPutAll(String key, Map<String, String> maps) {
        redisTemplate.opsForHash().putAll(key, maps);
    }

    /**
     * 获得哈希表中指定字段的值
     *
     * @param key
     * @param field
     * @return
     */
    public Object hGet(String key, String field) {
        return redisTemplate.opsForHash().get(key, field);
    }


    /**--------------------------------- list的相关操作----------------------------------- */


    /**--------------------------------- set的相关操作----------------------------------- */

    /**--------------------------------- zSet的相关操作----------------------------------- */
    /**
     * 添加元素，有序集合按照元素的score值由小到大排序
     *
     * @param key
     * @param value
     * @param score
     * @return
     */
    public Boolean zAdd(String key, String value, double score) {
        return redisTemplate.opsForZSet().add(key, value, score);
    }

    /**
     * 批量添加 将多个值集合添加到key中
     *
     * @param key
     * @param values
     * @return
     */
    public Long zAdd(String key, Set<ZSetOperations.TypedTuple<String>> values) {
        return redisTemplate.opsForZSet().add(key, values);
    }

    /**
     * 删除key中的value
     *
     * @param key
     * @param values 值数组
     * @return
     */
    public Long zRemove(String key, Object... values) {
        return redisTemplate.opsForZSet().remove(key, values);
    }

    /**
     * 获取集合中的元素，从小到大排序
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<String> zRange(String key, long start, long end) {
        return redisTemplate.opsForZSet().range(key, start, end);
    }

    /**
     * 获取集合中的元素，同时获取score的值，从小到大排序
     * @param key
     * @param start
     * @param end
     * @return
     */
    public Set<ZSetOperations.TypedTuple<String>> zRangeWithScore(String key,long start,long end){
        return redisTemplate.opsForZSet().rangeWithScores(key,start,end);
    }

}
