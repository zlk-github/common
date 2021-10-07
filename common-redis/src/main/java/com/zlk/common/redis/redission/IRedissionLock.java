package com.zlk.common.redis.redission;

import org.redisson.api.RFuture;

import java.util.concurrent.Future;

/**
 * @author likuan.zhou
 * @title: IRedissionLock
 * @projectName common
 * @description: Redission分布式锁接口
 * @date 2021/9/23/023 9:23
 */
public interface IRedissionLock {
    //===============================可重入锁（Reentrant Lock）=====================================
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    /**
     * 加redisson分布式锁（可重入锁--普通类型）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addLock(String key);

    /**
     * 释放redisson分布式锁（可重入锁--普通类型）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeLock(String key);

    /**
     * 加redisson分布式锁（可重入锁--普通类型--自定义过期）
     * @param key redis锁key
     * @param time 锁自动释放时间（单位S）
     * @return 执行结果 true成功，false失败
     */
     Boolean addLock(String key,long time);

    /**
     * 加redisson分布式锁（可重入锁--等待类型）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addTryLock(String key,long waitTime,long time);

    /**
     * 加redisson分布式锁（可重入锁-异步执行）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Future<Boolean> addTryLockAsync(String key, long waitTime, long time);

    //=============================== 公平锁（Fair Lock）=====================================
    // 基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，
    // 当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。
    /**
     * 加redisson分布式锁(重入锁--公平锁--普通类型)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addFairLock(String key);

    /**
     * 删除redisson分布式锁(重入锁--公平锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeFairLock(String key);

    /**
     * 加redisson分布式锁（可重入锁--公平锁--等待类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    Boolean addFairTryLock(String key,long waitTime,long time);

    /**
     * 加redisson分布式锁（可重入锁--公平锁--等待--异步类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     RFuture<Boolean> addFairTryLockAsync(String key, long waitTime, long time);

}
