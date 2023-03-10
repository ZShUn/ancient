package com.ancient.common.cache;

import com.ancient.common.entity.RuleEntity;
import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public class RuleCache {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleCache.class);

    private static final LoadingCache<String, RuleEntity> CAHCE = Caffeine.newBuilder()
            .expireAfterWrite(365 * 100, TimeUnit.DAYS)
            .initialCapacity(2)
            .maximumSize(10)
            .recordStats()
            .build(new CacheLoader<String, RuleEntity>() {

                @Override
                public RuleEntity load(String key) throws Exception {
                    return null;
                }

                @Override
                public @NonNull CompletableFuture<RuleEntity> asyncLoad(@NonNull String key, @NonNull Executor executor) {
                    return null;
                }
            });


    public static boolean put(String key, RuleEntity ruleEntity) {
        CAHCE.put(key, ruleEntity);

        return Boolean.TRUE;
    }

    public static RuleEntity get(String key) {
        try {
            return CAHCE.get(key);
        } catch (Exception e) {
            LOGGER.error("get rule cache error key={}", key, e);
            return null;
        }
    }

    public static boolean clear(String key) {
        CAHCE.invalidate(key);
        return Boolean.TRUE;
    }


}
