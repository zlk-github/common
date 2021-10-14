package com.zlk.common.redis.redisson;

import org.redisson.api.RFuture;
import org.redisson.api.RLock;

import java.util.concurrent.Future;

/**
 * @author likuan.zhou
 * @title: IRedissionLock
 * @projectName common
 * @description: Redission分布式锁接口
 * @date 2021/9/23/023 9:23
 */
public interface IRedissonLock {
    //===============================可重入锁（Reentrant Lock）=====================================
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    /**
     * 加redisson分布式锁（可重入锁--普通类型）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addLock(String key);

    /**
     * 释放redisson分布式锁（可重入锁）
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeLock(String key);

    /**
     * 加redisson分布式锁（可重入锁--过期类型（看门狗失效））
     * @param key redis锁key
     * @param time 锁自动释放时间（单位S）
     * @return 执行结果 true成功，false失败
     */
     Boolean addLock(String key,long time);

    /**
     * 加redisson分布式锁（可重入锁--过期（看门狗失效）--等待类型）
     * 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁。超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addTryLock(String key,long waitTime,long time);

    /**
     * 加redisson分布式锁（可重入锁--异步执行--过期（看门狗失效）--等待类型）
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
     * 加redisson分布式锁（可重入锁--公平锁--自定义过期（看门狗失效）--等待类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    Boolean addFairTryLock(String key,long waitTime,long time);

    /**
     * 加redisson分布式锁（可重入锁--公平锁--自定义过期（看门狗失效）--等待--异步类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     RFuture<Boolean> addFairTryLockAsync(String key, long waitTime, long time);

    //=============================== 联锁（MultiLock）=====================================
    // 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）
    /**
     * 加redisson分布式锁(联锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean addMultiLock(RLock... rLocks);

    /**
     * 解除redisson分布式锁(联锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean removeMultiLock(RLock... rLocks);

    //=============================== 红锁（RedLock）=====================================
    // 基于Redis的Redisson红锁RedissonRedLock对象实现了Redlock介绍的加锁算法。该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 红锁: 同时加锁，大部分锁节点加锁成功就算成功。locks.size() / 2 + 1为成功
    /**
     * 加redisson分布式锁(红锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean addRedLock(RLock... rLocks);

    /**
     * 解除redisson分布式锁(红锁--普通类型)
     * @param rLocks rLocks集合
     * @return 执行结果 true成功，false失败
     */
     Boolean removeRedLock(RLock... rLocks);

    //===============================读写锁（ReadWriteLock）=====================================
    // 基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。其中读锁和写锁都继承了RLock接口。
    // 读写锁: 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。
    // 读锁使用共享模式；写锁使用独占模式。
    /**
     * 加redisson分布式锁(可重入读锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addRwrLock(String key);

    /**
     * 加redisson分布式锁(可重入写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean addRwwLock(String key);

    /**
     * 解除redisson分布式锁(可重入读锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeRwrLock(String key);

    /**
     * 解除redisson分布式锁(可重入写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    Boolean removeRwwLock(String key);

    //===============================信号量（Semaphore 计数信号量）=====================================
    // 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 信号量: 当你的服务最大只能满足每秒1w的并发量时，我们可以使用信号量进行限流，当访问的请求超过1w时会进行等待（阻塞式或者非阻塞式，根据业务需求）。
    // --如停车位，进出场地（车位有限）。唯一计数。
    /**
     * 信号量初始值设置
     * @param key redis锁key
     * @param initVal 信号量初始值
     * @return 执行结果 true成功，false失败
     */
    Boolean trySetPermits(String key,int initVal);

    /**
     * 信号量使用
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
     Boolean useSemaphore(String key,int val);

    /**
     * 信号量使用
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean useSemaphore(String key);

    /**
     * 释放信号量
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
     Boolean releaseSemaphore(String key,int val);

    /**
     * 释放信号量
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
     Boolean releaseSemaphore(String key);

    //===============================可过期性信号量（PermitExpirableSemaphore）=====================================
    // 基于Redis的Redisson可过期性信号量（PermitExpirableSemaphore）是在RSemaphore对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 可过期性信号量: ID来辨识的信号量设置有效时间

    /**
     * 可过期性信号量初始值设置
     * @param key redis锁key
     * @param initVal 信号量初始值
     * @return 执行结果 true成功，false失败
     */
     Boolean peTrySetPermits(String key,int initVal);

    /**
     * 可过期性信号量
     * @param key 可过期性信号量标识
     * @param time 信号量过期时间 （单位s）
     * @return 执行结果 true成功，false失败
     */
     Boolean peSemaphore(String key,long time);

    /**
     * 可过期性信号量释放
     * @param key 可过期性信号量标识
     * @return 执行结果 true成功，false失败
     */
     Boolean releasePeSemaphore(String key);

    //=============================== 闭锁（CountDownLatch）=====================================
    // 基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象RCountDownLatch采用了与java.util.concurrent.CountDownLatch相似的接口和用法。
    // 闭锁: 计数清零时执行（如班上20个同学全部离开才能锁教室门）

    /**
     * 闭锁 （初始值）
     * @param key key
     * @param val 闭锁的计数初始值（如教室总人数）
     * @return 执行结果 true成功，false失败
     */
     Boolean trySetCount(String key,long val);

    /**
     * 闭锁 （关门上锁）
     * 当闭锁设置的初始值全部释放（调removeCountDownLatch方法使trySetCount清空为0），才往下执行，否则等待。
     * @param key key
     * @return 执行结果 true成功，false失败
     */
     Boolean addCountDownLatch(String key);

    /**
     * 闭锁 （锁计数减一）
     * @param key key
     * @return 执行结果 true成功，false失败
     */
     Boolean removeCountDownLatch(String key);
}
