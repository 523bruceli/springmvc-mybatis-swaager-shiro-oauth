package com.hjzgg.util;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


public class RedisUtil {

    private static ShardedJedisPool shardedJedisPool;

    private static ShardedJedis shardedJedis;

    /**
     * 获取redis
     *
     * @return
     */
    public static ShardedJedis getRedis() {
        if (null == shardedJedis) {
            shardedJedis = shardedJedisPool.getResource();
        }
        return shardedJedis;
    }

    public void setShardedJedisPool(ShardedJedisPool shardedJedisPool) {
        RedisUtil.shardedJedisPool = shardedJedisPool;
    }

    public void setShardedJedis(ShardedJedis shardedJedis) {
        RedisUtil.shardedJedis = shardedJedis;
    }
}
