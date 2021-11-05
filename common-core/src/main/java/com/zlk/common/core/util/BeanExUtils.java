package com.zlk.common.core.util;

import com.google.common.base.Function;
import com.zlk.common.core.page.PageBean;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author likuan.zhou
 * @title: BeanExUtils
 * @projectName common
 * @description: Bean 操作
 * @date 2021/9/14/014 19:56
 */
@Data
@Slf4j
public class BeanExUtils {
    /**
     * 复制对象
     * @param source 源对象
     * @param clazz 复制类型
     * @param <T> class
     * @return 复制对象返回
     */
    public static <T> T copyProperties(Object source, Class<T> clazz) {
        if (source == null) {
            return null;
        }
        T instance = null;
        try {
            instance = clazz.newInstance();
            BeanUtils.copyProperties(source,instance);
        } catch (InstantiationException | IllegalAccessException e) {
            log.error("对象复制异常",e);
        }
        return instance;
    }

    /**
     * 复制list
     * @param source 源对象
     * @param clazz 复制类型
     * @param <T> class
     * @return 复制list返回
     */
    public static <T> List<T> copyList(List<?> source, Class<T> clazz) {
        if (CollectionUtils.isEmpty(source)) {
            return Collections.emptyList();
        }
        List<T> targetList = new ArrayList<>();
        Iterator<?> iterator = source.iterator();
        try {
            while (iterator.hasNext()) {
                T instance = clazz.newInstance();
                BeanUtils.copyProperties(iterator.next(),instance);
                targetList.add(instance);
            }
        } catch (InstantiationException | IllegalAccessException e) {
           log.error("对象列表复制异常",e);
        }
        return targetList;
    }

    /**
     * page对象转换
     * @param source 源page
     * @param convert 转换
     * @param <P> 转换前类型
     * @param <V> 转换后类型
     * @return 转换后page对象
     */
    public static <P,V> PageBean<V> copyPageBean(PageBean<P> source, Function<P,V> convert){
        if (source == null) {
            return new PageBean<V>();
        }
        PageBean<V> pageBean = new PageBean<>();
        List<V> voList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(source.getList())) {
            source.getList().forEach(dto->{
                V vo = convert.apply(dto);
                voList.add(vo);
            });
        }
        pageBean.setList(voList);
        pageBean.setTotal(source.getTotal());
        pageBean.setPageSize(source.getSize());
        pageBean.setPageNo(source.getPageNo());
        pageBean.setPages(source.getPages());
        return pageBean;
    }
}
