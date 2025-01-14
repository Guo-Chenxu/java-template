package com.guochenxu.javatemplate.utils;

import com.guochenxu.javatemplate.config.RedissonConfig;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * 分布式锁处理
 *
 * @author faneeguo
 */
@Component
@Slf4j
public class RedissonLockUtil {

    private RedissonClient redissonClient;

    private final RedissonConfig redissonConfig;

    public RedissonLockUtil(RedissonConfig _redissonConfig) {
        this.redissonConfig = _redissonConfig;
    }

    @PostConstruct
    private void init() {
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redissonConfig.getAddr())
                .setPassword(redissonConfig.getPassword())
                .setDatabase(redissonConfig.getDb())
                .setConnectionMinimumIdleSize(redissonConfig.getConnectPoolSize())
                .setConnectionPoolSize(redissonConfig.getConnectPoolSize());
        redissonClient = Redisson.create(config);
    }

    /**
     * 加锁
     * lock是当获取锁失败时会阻塞当前进程，如果没有带参数设置过期时间则是30秒后自动解锁。
     *
     * @param lockKey
     * @return
     */
    public RLock lock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock();
        return lock;
    }

    /**
     * 释放锁
     *
     * @param lockKey
     */
    public void unlock(String lockKey) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.unlock();
    }

    /**
     * 释放锁
     *
     * @param lock
     */
    public void unlock(RLock lock) {
        lock.unlock();
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param timeout 超时时间   单位：秒
     */
    public RLock lock(String lockKey, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, TimeUnit.SECONDS);
        return lock;
    }

    /**
     * 带超时的锁
     *
     * @param lockKey
     * @param unit    时间单位
     * @param timeout 超时时间
     */
    public RLock lock(String lockKey, TimeUnit unit, int timeout) {
        RLock lock = redissonClient.getLock(lockKey);
        lock.lock(timeout, unit);
        return lock;
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public boolean tryLock(String lockKey, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取锁
     *
     * @param lockKey
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public boolean tryLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getLock(lockKey);
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 尝试获取写操作锁
     *
     * @param lockKey   锁KEY
     * @param unit      时间单位
     * @param waitTime  最多等待时间
     * @param leaseTime 上锁后自动释放锁时间
     * @return
     */
    public boolean tryWriteLock(String lockKey, TimeUnit unit, int waitTime, int leaseTime) {
        RLock lock = redissonClient.getReadWriteLock(lockKey).writeLock();
        try {
            return lock.tryLock(waitTime, leaseTime, unit);
        } catch (InterruptedException e) {
            return false;
        }
    }

    /**
     * 得到锁并且处理业务
     *
     * @param lockKey      锁名称
     * @param waitSeconds  等待时长
     * @param leaseSeconds 释放时长
     * @param handler      处理器
     * @return 返回处理结果
     */
    public boolean getLockAndhandle(String lockKey, long waitSeconds, long leaseSeconds,
                                    Supplier<Boolean> handler) {
        //初始化锁的对象
        RLock rLock = redissonClient.getLock(lockKey);
        try {
            //尝试加锁, 最多等待5秒
            boolean lock = rLock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS);
            if (lock) {
                return handler.get();
            } else {
                throw new RuntimeException("获取同步锁失败,请稍候重试");
            }
        } catch (InterruptedException e) {
            log.error("获取锁异常 e:{}", e.getMessage());
        } finally {
            //是锁定状态，并且是当前执行线程的锁，释放锁
            try {
                if (rLock.isHeldByCurrentThread() && rLock.isLocked()) {
                    rLock.unlock();
                } else {
                    log.info("no locked");
                }
            } catch (Exception e) {
                log.error("e:" + e.getMessage());
            }
        }
        return false;
    }

    /**
     * 得到写锁并且处理业务
     *
     * @param lockKey      锁名称
     * @param waitSeconds  等待时长
     * @param leaseSeconds 释放时长
     * @return 返回处理结果
     */
    public boolean getWriteLockAndhandle(String lockKey, long waitSeconds, long leaseSeconds,
                                         Supplier<Boolean> handler) {
        //初始化锁的对象
        RLock rLock = redissonClient.getReadWriteLock(lockKey).writeLock();
        try {
            //尝试加锁, 最多等待5秒
            boolean lock = rLock.tryLock(waitSeconds, leaseSeconds, TimeUnit.SECONDS);
            if (lock) {
                log.info("获取到锁，开始写数据");
                return handler.get();
            } else {
                throw new RuntimeException("获取同步锁失败,请稍候重试");
            }
        } catch (InterruptedException e) {
            log.error("获取锁异常 e:{}", e.getMessage());
        } finally {
            //是锁定状态，并且是当前执行线程的锁，释放锁
            try {
                if (rLock.isHeldByCurrentThread() && rLock.isLocked()) {
                    rLock.unlock();
                } else {
                    log.info("no locked");
                }
            } catch (Exception e) {
                log.error("e:" + e.getMessage());
            }
        }
        return false;
    }

}