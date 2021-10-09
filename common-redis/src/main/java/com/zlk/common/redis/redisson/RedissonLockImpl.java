package com.zlk.common.redis.redisson;

import lombok.extern.slf4j.Slf4j;
import org.redisson.RedissonMultiLock;
import org.redisson.RedissonRedLock;
import org.redisson.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author likuan.zhou
 * @title: RedissonLockImpl
 * @projectName common
 * @description: Redission分布式锁接口实现
 * @date 2021/9/27/027 8:57
 */
@Slf4j
@Component
public class RedissonLockImpl implements IRedissonLock {
    @Autowired
    private RedissonClient redisson;

    //===============================可重入锁（Reentrant Lock）=====================================
    //基于Redis的Redisson分布式可重入锁RLock Java对象实现了java.util.concurrent.locks.Lock接口。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口
    // 可重入锁: 避免死锁，可重复递归调用的锁,同一线程外层函数获取锁后,内层递归函数仍然可以获取锁,并且不发生死锁(前提是同一个对象或者class)

    /**
     * 加redisson分布式锁
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
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
    @Override
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
    @Override
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
     * 加redisson分布式锁（可重入锁--等待类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
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
    @Override
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
    //公平锁: 多线程按照申请锁的顺序获取锁。--（先进先出原则为公平锁）

    /**
     * 加redisson分布式锁(重入锁--公平锁--普通类型)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addFairLock(String key) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            fairLock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 删除redisson分布式锁(重入锁-公平锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean removeFairLock(String key) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            fairLock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁（可重入锁--公平锁--等待类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public Boolean addFairTryLock(String key,long waitTime,long time) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            //上锁以后time秒自动解锁（可重入锁--公平锁--自定义过期）
            //fairLock.tryLock(time,TimeUnit.SECONDS);
            // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁
            fairLock.tryLock(waitTime,time,TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 加redisson分布式锁（可重入锁--公平锁--等待--异步类型）
     * @param time 上锁以后time秒自动解锁。
     * @param waitTime 尝试加锁，最多等待waitTime秒（单位S）,超过时间未获取到锁返回false.否则返回true。去执行下面逻辑。
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    @Override
    public RFuture<Boolean>  addFairTryLockAsync(String key,long waitTime,long time) {
        try {
            RLock fairLock = redisson.getFairLock(key);
            //普通异步
            //fairLock.lockAsync();
            //异步，上锁以后time秒自动解锁
            //fairLock.lockAsync(time, TimeUnit.SECONDS);
            // 尝试加锁，最多等待waitTime秒，上锁以后time秒自动解锁
            RFuture<Boolean> future = fairLock.tryLockAsync(waitTime, time, TimeUnit.SECONDS);
            return future;
        }catch (Exception ex) {
            log.error("分布式锁--公平锁上锁失败。key:{}",key,ex);
        }
        return null;
    }

    //=============================== 联锁（MultiLock）=====================================
    // 基于Redis的Redisson分布式联锁RedissonMultiLock对象可以将多个RLock对象关联为一个联锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 联锁: 保证同时加锁，且所有锁都需要上锁成功才算成功（里面具体的锁可以自己选择，然后做实现。下面以普通可重入锁来实现）

    /**
     * 加redisson分布式锁(联锁--普通类型)
     * @param keySet redis锁key集合
     * @return 执行结果 true成功，false失败
     */
    public Boolean addMultiLock(Set<String> keySet) {
        if (CollectionUtils.isEmpty(keySet)) {
            return false;
        }
        RedissonMultiLock lock;
        try {
            Set<RLock> lockSet = new HashSet<>();
            keySet.forEach(key->{
                lockSet.add(redisson.getLock(key));
            });
            lock = new RedissonMultiLock(lockSet.stream().iterator().next());
            // 同时加锁：lock1 lock2 lock3.......
            // 联锁 所有的锁都上锁成功才算成功。
            lock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--联锁上锁失败。keySet:{}",keySet,ex);
        }
        return false;
    }

    /**
     * 解除redisson分布式锁(联锁--普通类型)
     * @param keySet redis锁key集合
     * @return 执行结果 true成功，false失败
     */
    public Boolean removeMultiLock(Set<String> keySet) {
        if (CollectionUtils.isEmpty(keySet)) {
            return false;
        }
        RedissonMultiLock lock;
        try {
            Set<RLock> lockSet = new HashSet<>();
            keySet.forEach(key->{
                lockSet.add(redisson.getLock(key));
            });
            lock = new RedissonMultiLock(lockSet.stream().iterator().next());
            lock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--联锁解锁失败。keySet:{}",keySet,ex);
        }
        return false;
    }

    // 等待实现不赘述
    //RedissonRedLock lock = new RedissonMultiLock(lock1, lock2, lock3);
    // 给lock1，lock2，lock3加锁，如果没有手动解开的话，10秒钟后将会自动解开
    //lock.lock(10, TimeUnit.SECONDS);

    // 为加锁等待100秒时间，并在加锁成功10秒钟后自动解开
    //boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);


    //=============================== 红锁（RedLock）=====================================
    // 基于Redis的Redisson红锁RedissonRedLock对象实现了Redlock介绍的加锁算法。该对象也可以用来将多个RLock对象关联为一个红锁，每个RLock对象实例可以来自于不同的Redisson实例。
    // 联锁: 同时加锁，大部分锁节点加锁成功就算成功。

    /**
     * 加redisson分布式锁(红锁--普通类型)
     * @param keySet redis锁key集合
     * @return 执行结果 true成功，false失败
     */
    public Boolean addRedLock(Set<String> keySet) {
        if (CollectionUtils.isEmpty(keySet)) {
            return false;
        }
        RedissonRedLock lock;
        try {
            Set<RLock> lockSet = new HashSet<>();
            keySet.forEach(key->{
                lockSet.add(redisson.getLock(key));
            });
            lock = new RedissonRedLock(lockSet.stream().iterator().next());
            // 同时加锁：lock1 lock2 lock3.......
            // 红锁在大部分节点上加锁成功就算成功。
            lock.lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--红锁上锁失败。keySet:{}",keySet,ex);
        }
        return false;
    }

    /**
     * 解除redisson分布式锁(红锁--普通类型)
     * @param keySet redis锁key集合
     * @return 执行结果 true成功，false失败
     */
    public Boolean removeRedLock(Set<String> keySet) {
        if (CollectionUtils.isEmpty(keySet)) {
            return false;
        }
        RedissonRedLock lock;
        try {
            Set<RLock> lockSet = new HashSet<>();
            keySet.forEach(key->{
                lockSet.add(redisson.getLock(key));
            });
            lock = new RedissonRedLock(lockSet.stream().iterator().next());
            lock.unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--红锁解锁失败。keySet:{}",keySet,ex);
        }
        return false;
    }

    // 等待实现不赘述
    //RedissonRedLock lock = new RedissonRedLock(lock1, lock2, lock3);
    // 给lock1，lock2，lock3加锁，如果没有手动解开的话，10秒钟后将会自动解开
    //lock.lock(10, TimeUnit.SECONDS);

    // 为加锁等待100秒时间，并在加锁成功10秒钟后自动解开
    //boolean res = lock.tryLock(100, 10, TimeUnit.SECONDS);


    //===============================读写锁（ReadWriteLock）=====================================
    // 基于Redis的Redisson分布式可重入读写锁RReadWriteLock Java对象实现了java.util.concurrent.locks.ReadWriteLock接口。其中读锁和写锁都继承了RLock接口。
    // 读写锁: 分布式可重入读写锁允许同时有多个读锁和一个写锁处于加锁状态。

    /**
     * 加redisson分布式锁(可重入读写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Boolean addRwLock(String key) {
        RReadWriteLock readWriteLock;
        try {
            readWriteLock = redisson.getReadWriteLock(key);
            // 读
            readWriteLock.readLock().lock();
            // 或者 写
            // readWriteLock.writeLock().lock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--可重入读写锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 解除redisson分布式锁(可重入读写锁)
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Boolean removeRwLock(String key) {
        try {
            RReadWriteLock readWriteLock = redisson.getReadWriteLock(key);
            readWriteLock.readLock().unlock();
            return true;
        }catch (Exception ex) {
            log.error("分布式锁--可重入读写锁上锁失败。key:{}",key,ex);
        }
        return false;
    }

    // 等待实现不赘述
    //RReadWriteLock rwlock = redisson.getReadWriteLock(key);
    //rwlock.readLock().lock(10, TimeUnit.SECONDS);
    // 或
    //rwlock.writeLock().lock(10, TimeUnit.SECONDS);

    // 尝试加锁，最多等待100秒，上锁以后10秒自动解锁
    //boolean res = rwlock.readLock().tryLock(100, 10, TimeUnit.SECONDS);
    // 或
    //boolean res = rwlock.writeLock().tryLock(100, 10, TimeUnit.SECONDS);


    //===============================信号量（Semaphore 计数信号量）=====================================
    // 基于Redis的Redisson的分布式信号量（Semaphore）Java对象RSemaphore采用了与java.util.concurrent.Semaphore相似的接口和用法。同时还提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 信号量: 当你的服务最大只能满足每秒1w的并发量时，我们可以使用信号量进行限流，当访问的请求超过1w时会进行等待（阻塞式或者非阻塞式，根据业务需求）。
    // --如停车位，进出场地（车位有限）。唯一计数。

    /**
     * 信号量使用
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
    public Boolean useSemaphore(String key,int val) {
        try {
            //信号量
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值-val，如果为0则一直等待，直到信号量>0
            semaphore.acquire(val);
            return true;
        }catch (Exception ex) {
            log.error("信号量使用失败。key:{},val:{}",key,val,ex);
        }
        return false;
    }

    /**
     * 信号量使用
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Boolean useSemaphore(String key) {
        try {
            //信号量
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值-1，如果为0则一直等待，直到信号量>0
            semaphore.acquire();
            return true;
        }catch (Exception ex) {
            log.error("信号量使用失败。key:{}",key,ex);
        }
        return false;
    }


    /**
     * 释放信号量使用
     * @param key redis锁key
     * @param val 信号量减少值
     * @return 执行结果 true成功，false失败
     */
    public Boolean releaseSemaphore(String key,int val) {
        try {
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值+val，也就是释放信号量
            semaphore.release(val);
            return true;
        }catch (Exception ex) {
            log.error("释放信号量失败。key:{},val:{}",key,val,ex);
        }
        return false;
    }

    /**
     * 释放信号量使用
     * @param key redis锁key
     * @return 执行结果 true成功，false失败
     */
    public Boolean releaseSemaphore(String key) {
        try {
            RSemaphore semaphore = redisson.getSemaphore(key);
            //这里会将信号量里面的值+1，也就是释放信号量
            semaphore.release();
            return true;
        }catch (Exception ex) {
            log.error("释放信号量失败。key:{}",key,ex);
        }
        return false;
    }

    // 等待与异步实现不赘述
    /*   RSemaphore semaphore = redisson.getSemaphore(key);
    semaphore.acquire();
    //或
    semaphore.acquireAsync();
    semaphore.acquire(23);
    semaphore.tryAcquire();
    //或
    semaphore.tryAcquireAsync();
    semaphore.tryAcquire(23, TimeUnit.SECONDS);
    //或
    semaphore.tryAcquireAsync(23, TimeUnit.SECONDS);
    semaphore.release(10);
    semaphore.release();
    //或
    semaphore.releaseAsync();*/

    //===============================可过期性信号量（PermitExpirableSemaphore）=====================================
    // 基于Redis的Redisson可过期性信号量（PermitExpirableSemaphore）是在RSemaphore对象的基础上，为每个信号增加了一个过期时间。每个信号可以通过独立的ID来辨识，释放时只能通过提交这个ID才能释放。它提供了异步（Async）、反射式（Reactive）和RxJava2标准的接口。
    // 可过期性信号量: ID来辨识的信号量设置有效时间

    /**
     * 可过期性信号量
     * @param key 可过期性信号量标识
     * @param time 信号量过期时间 （单位s）
     * @return 执行结果 true成功，false失败
     */
    public Boolean peSemaphore(String key,long time) {
        try {
            RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore(key);
            // 获取一个信号，有效期只有time秒钟。
            semaphore.acquire(time,TimeUnit.SECONDS);
            return true;
        }catch (Exception ex) {
            log.error("可过期性信号量使用失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 可过期性信号量释放
     * @param key 可过期性信号量标识
     * @return 执行结果 true成功，false失败
     */
    public Boolean releasePeSemaphore(String key) {
        try {
            RPermitExpirableSemaphore semaphore = redisson.getPermitExpirableSemaphore(key);
            // 通过标识释放过期信号量
            semaphore.release(key);
            return true;
        }catch (Exception ex) {
            log.error("可过期性信号量释放失败。key:{}",key,ex);
        }
        return false;
    }

    //=============================== 闭锁（CountDownLatch）=====================================
    // 基于Redisson的Redisson分布式闭锁（CountDownLatch）Java对象RCountDownLatch采用了与java.util.concurrent.CountDownLatch相似的接口和用法。
    // 闭锁: 计数清零时执行（如班上20个同学全部离开才能锁教室门）

    /**
     * 闭锁 （加锁）
     * @param key key
     * @param val 闭锁的计数初始值（如教室总人数）
     * @return 执行结果 true成功，false失败
     */
    public Boolean addCountDownLatch(String key,long val) {
        try {
            RCountDownLatch latch = redisson.getCountDownLatch("anyCountDownLatch");
            //闭锁的计数初始值（如教室总人数）
            latch.trySetCount(val);
            // 当闭锁设置的初始值全部释放（调removeCountDownLatch方法使trySetCount清空为0），才往下执行，否则等待。
            latch.await();
            return true;
        }catch (Exception ex) {
            log.error("闭锁失败。key:{}",key,ex);
        }
        return false;
    }

    /**
     * 闭锁 （锁计数减一）
     * @param key key
     * @return 执行结果 true成功，false失败
     */
    public Boolean removeCountDownLatch(String key) {
        try {
            RCountDownLatch latch = redisson.getCountDownLatch(key);
            // 表示锁计数减一（如教室离开一人）
            latch.countDown();
            return true;
        }catch (Exception ex) {
            log.error("闭锁（锁计数减一）失败。key:{}",key,ex);
        }
        return false;
    }




}
