package com.zlk.common.redis.redission;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author likuan.zhou
 * @title: RedissionLockImpl
 * @projectName common
 * @description: Redission分布式锁接口实现
 * @date 2021/9/27/027 8:57
 */
@Slf4j
public class RedissionLockImpl {
    @Autowired
    private Redisson redisson;

    //===============================可重入锁（Reentrant Lock）=====================================
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    /**
     * 加redisson分布式锁
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Boolean addLock(String key) {
        try {
            RLock lock = redisson.getLock(key);
            lock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 释放redisson分布式锁
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Boolean removeLock(String key) {
        try {
            RLock lock = redisson.getLock(key);
            lock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁
     * @param key redis锁key
     * @param time 锁自动释放时间（单位S）
     * @return 执行结果 true成功，false失败
     */
    public Boolean addLock(String key,long time) {
        try {
            RLock lock = redisson.getLock(key);
            // 加锁以后time秒钟自动解锁
            // 无需调用unlock方法手动解锁
            lock.lock(time, TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return false;
    }


    /**
     * 加redisson分布式锁（可重入锁）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Boolean addTryLock(String key,long waitTime,long time) {
        try {
            RLock lock = redisson.getLock(key);
            // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
            // 需要unlock解锁
            return lock.tryLock(100, 10, TimeUnit.SECONDS);
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁（可重入锁-异步执行）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Future<Boolean> addTryLockAsync(String key,long waitTime,long time) {
        try {
            RLock lock = redisson.getLock(key);
            //lock.lockAsync();
            //lock.lockAsync(10, TimeUnit.SECONDS);
            // 可重入锁-异步执行
            Future<Boolean> res = lock.tryLockAsync(100, 10, TimeUnit.SECONDS);
        }catch (Exception ex) {
            log.error("分布式锁上锁失败。key:{}",key,ex);
        }
        return null;
    }

    //=============================== 公平锁（Fair Lock）=====================================
    //基于Redis的Redisson分布式可重入公平锁也是实现了java.util.concurrent.locks.Lock接口的一种RLock对象。。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    //它保证了当多个Redisson客户端线程同时请求加锁时，优先分配给先发出请求的线程。所有请求线程会在一个队列中排队，当某个线程出现宕机时，Redisson会等待5秒后继续下一个线程，也就是说如果前面有5个线程都处于等待状态，那么后面的线程会等待至少25秒。

}
