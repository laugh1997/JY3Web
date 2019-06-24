package com.neuedu.commen;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.apache.commons.lang3.StringUtils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
//初始化Guava
public class GuavaCache {

    public static LoadingCache<String,String> cacheBuilder = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(1000).expireAfterAccess(1, TimeUnit.DAYS).build(
        new CacheLoader<String, String>() {
        @Override
        public String load(String key) throws Exception {
            return null;
        }
    });


    public static void putCache(String key,String value)
    {
        cacheBuilder.put(key,value);
    }

    public static String getCache(String key)
    {
        String s = null;
        try {
            s = cacheBuilder.get(key);
            if ("null".equals(s))
            {
                return null;
            }
            return s;
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return s;
    }
}
