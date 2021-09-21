package com.zlk.common.redis.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author likuan.zhou
 * @title: RedisUtil
 * @projectName common
 * @description: redis操作工具类
 * @date 2021/9/16/016 19:25
 */
@Component
@Slf4j
public class RedisUtil {
    @Autowired
    private RedisTemplate<String,Object> redisTemplate;

    /**
     * 删除缓存
     * @param key redis键
     * @return 执行结果 true成功，false失败
     */
    public Boolean del(String key){
        try {
            return redisTemplate.delete(key);
        }catch (Exception ex) {
            log.error("删除redis缓存失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 批量删除缓存
     * @param keys redis键set集合
     * @return 执行结果 true成功，false失败
     */
    public Long del(Set<String> keys){
        return redisTemplate.delete(keys);
    }

    //==========================String==============================
    /**
     * 入普通缓存（字符串类型）
     * @param key redis键
     * @param value 值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean set(String key,Object value,long time){
        try {
            redisTemplate.opsForValue().set(key,value,time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 入普通缓存（字符串类型）
     * --key不存在则新增，key已存在则覆盖原来值。
     * @param key redis键
     * @param value 值
     * @return 执行结果 true成功，false失败
     */
    public Boolean set(String key,Object value){
        try {
            redisTemplate.opsForValue().set(key,value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 通过key获取普通缓存（字符串类型）
     * @param key redis键
     * @return 执行结果 true成功，false失败
     */
    public Object get(String key){
        try {
            return key == null ? null : redisTemplate.opsForValue().get(key);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{}",key,ex);
        }
        return null;
    }


    //==========================hash（MAP）==============================
    /**
     * 入map到缓存（hash表）
     * @param key redis键
     * @param map map值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public <T,V> Boolean hmSet(String key, Map<T,V> map, long time){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,map,ex);
        }
        return false;
    }

    /**
     * 入map到缓存（hash表）
     * @param key redis键
     * @param map map值
     * @return 执行结果 true成功，false失败
     */
    public <T,V> Boolean hmSet(String key, Map<T,V> map){
        try {
            redisTemplate.opsForHash().putAll(key,map);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,map,ex);
        }
        return false;
    }

    /**
     * 入键值对到map缓存（hash表）
     * @param key redis键
     * @param item map中键
     * @param value map中item对应值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean hSet(String key,Object item,Object value,long time){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},item:{},value:{}",key,item,value,ex);
        }
        return false;
    }

    /**
     * 入键值对到map缓存（hash表）
     * @param key redis键
     * @param item map中键
     * @param value map中item对应值
     * @return 执行结果 true成功，false失败
     */
    public Boolean  hSet(String key,Object item,Object value){
        try {
            redisTemplate.opsForHash().put(key,item,value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},item:{},value:{}",key,item,value,ex);
        }
        return false;
    }

    /**
     * 获取key对应map的item键值（hash表）
     * @param key redis键
     * @param item map键
     * @return key对应map的item键值
     */
    public Object hGet(String key,Object item){
        try {
            return redisTemplate.opsForHash().get(key,item);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{}",key,ex);
        }
        return null;
    }

    /**
     * 获取key对应整个map（hash表）
     * @param key redis键
     * @return key对应整个map
     */
    public Map<Object,Object> hmGet(String key){
        try {
            return key == null ? Collections.emptyMap() : redisTemplate.opsForHash().entries(key);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{}",key,ex);
        }
        return null;
    }

    /**
     *  删除key对应map中某些键值（hash表）
     * @param key redis键
     * @return items map<item,value>
     */
    public void hDel(String key,Object... items){
        redisTemplate.opsForHash().delete(key, items);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public long hIncr(String key, Object item, long by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hIncr(String key, Object item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public long hDecr(String key, Object item, long by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * hash递减
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hDecr(String key, Object item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    /**
     * 判断hash表中是否有该项的值
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, Object item) {
        try {
            return redisTemplate.opsForHash().hasKey(key, item);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    //==========================链表list==============================
    /**
     * 从右边添加list（链表list）
     * @param key redis键
     * @param list list链表
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public <T> Boolean llSet(String key, List<T> list, long time){
        try {
            redisTemplate.opsForList().rightPushAll(key,list);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,list,ex);
        }
        return false;
    }

    /**
     * 从右边添加list（链表list）
     * @param key redis键
     * @param list list链表
     * @return 执行结果 true成功，false失败
     */
    public <T> Boolean llSet(String key, List<T> list){
        try {
            redisTemplate.opsForList().rightPushAll(key,list);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,list,ex);
        }
        return false;
    }

    /**
     * 从右边添加value到list（链表list）
     * @param key redis键
     * @param value value
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean lSet(String key, Object value,Long time){
        try {
            redisTemplate.boundListOps(key).rightPush(value);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 从右边添加value到list（链表list）
     * @param key redis键
     * @param value value
     * @return 执行结果 true成功，false失败
     */
    public Boolean lSet(String key, Object value){
        try {
            redisTemplate.boundListOps(key).rightPush(value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},list:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 获取链表中索引起止位置段（链表list）
     * @param key redis键
     * @param start 开始位置
     * @param end 结束位置
     * @return key对应map的item键值
     */
    public Object lGet(String key,long start,long end){
        try {
            return redisTemplate.boundListOps(key).range(start,end);
        }catch (Exception ex) {
            log.error("获取redis缓存失败。key:{},start:{},end:{}",key,start,end,ex);
        }
        return null;
    }

    /**
     * 通过索引 获取list中的值（链表list）
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.boundListOps(key).index(index);
        } catch (Exception ex) {
            log.error("获取redis缓存list失败。key:{}",key,ex);
        }
        return null;
    }

    /**
     * 获取链表长度（链表list）
     * @param key redis键
     * @return key对应map的item键值
     */
    public Long lSize(String key){
        try {
            return redisTemplate.boundListOps(key).size();
        }catch (Exception ex) {
            log.error("获取redis缓存list长度失败。key:{}",key,ex);
        }
        return 0L;
    }

    /**
     * 根据索引修改list中的某条数据（链表list）
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 执行结果
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.boundListOps(key).set(index, value);
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 删除列表中值为value的元素，总共删除count次(不常用)
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return 移除的个数
     */
    public Long lRemove(String key, long index, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(key,index,value);
            return Long.valueOf(remove);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 删除list首尾，只保留 [start, end] 之间的值，闭区间
     * @param key   键
     * @param start 索引开始
     * @param end 索引结束
     * @return 移除的个数
     */
    public void lTrim(String key, long start, long end) {
        try {
            redisTemplate.opsForList().trim(key,start,end);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }


    //==========================无序Set==============================
    /**
     * 新增无序Set（无序Set）
     * @param key redis键
     * @param set set
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Set<Object> set, long time){
        try {
            redisTemplate.opsForSet().add(key,set);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},set:{}",key,set,ex);
        }
        return false;
    }

    /**
     * 新增无序Set（无序Set）
     * @param key redis键
     * @param set set
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Set<Object> set){
        try {
            redisTemplate.opsForSet().add(key,set);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},set:{}",key,set,ex);
        }
        return false;
    }

    /**
     * 往Set添加value（无序Set）
     * @param key redis键
     * @param value 值
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Object value, long time){
        try {
            redisTemplate.boundSetOps(key).add(value);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 往Set添加value（无序Set）
     * @param key redis键
     * @param value 值
     * @return 执行结果 true成功，false失败
     */
    public Boolean sSet(String key, Object value){
        try {
            redisTemplate.boundSetOps(key).add(value);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 获取set缓存的长度（无序Set）
     * @param key 键
     * @return
     */
    public Long sSize(String key) {
        try {
            return redisTemplate.boundSetOps(key).size();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 获取set （无序Set）
     * @param key 键
     * @return set
     */
    public Object sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 移除set中值为value的（无序Set）
     * @param key    键
     * @param set 值 可以是多个
     */
    public <T> long sRemove(String key, Set<T> set) {

        Long remove = redisTemplate.boundSetOps(key).remove(set);
        return remove;
    }

    //==========================有序zSet==============================
    /**
     * 入zset缓存（有序zSet）
     * @param key redis键
     * @param value item
     * @param sort 排序
     * @param time 过期时间（单位秒）
     * @return 执行结果 true成功，false失败
     */
    public Boolean zsSet(String key, Object value,long sort,long time){
        try {
            redisTemplate.opsForZSet().add(key,value,sort);
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("写入redis缓存失败。key:{},value:{}",key,value,ex);
        }
        return false;
    }

    /**
     * 获取zset缓存的长度（有序zSet）
     * @param key redis键
     * @return
     */
    public Long zsSize(String key) {
        try {
            return redisTemplate.boundZSetOps(key).size();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 加减分（有序zSet）
     * @param key  redis键
     * @param value 值
     * @param sore 加分数
     * @return  加后分数
     */
    private Double zsIncr(String key,Object value,long sore) {
        return  redisTemplate.boundZSetOps(key).incrementScore(value, sore);
    }

    /**
     * 获取Zset中某项分数（有序zSet）
     * @param key  redis键
     * @param value 值
     * @param sore 分数
     */
    private Double incrementScore(String key,Object value,long sore) {
        return  redisTemplate.boundZSetOps(key).score(value);
    }

    /**
     * 获取zset （有序zSet）
     * @param key 键
     * @return set
     */
    public Object zsGet(String key,long min,long max) {
        try {
            //从小到大
            return redisTemplate.opsForZSet().rangeByScore(key,min,max);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return 0L;
    }

    /**
     * 移除zset中值为value的（有序zSet）
     * @param key    键
     * @param set 值 可以是多个
     */
    public long zsRemove(String key, Set<Object> set) {
        Long remove = redisTemplate.boundZSetOps(key).remove(set);
        return remove;
    }
}
