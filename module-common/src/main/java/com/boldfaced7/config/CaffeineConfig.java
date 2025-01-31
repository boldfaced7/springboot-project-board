//package com.boldfaced7.config.config;
//
//import com.github.benmanes.caffeine.cache.Caffeine;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.caffeine.CaffeineCacheManager;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import java.util.concurrent.TimeUnit;
//
//@EnableCaching
//@Configuration
//public class CaffeineConfig {
//
//    @Bean
//    public Caffeine<Object, Object> caffeineCacheConfig() {
//        return Caffeine.newBuilder()
//                .expireAfterWrite(60, TimeUnit.MINUTES)
//                .maximumSize(1000);
//    }
//
//    @Bean
//    public CacheManager cacheManager(Caffeine<Object, Object> caffeine) {
//        CaffeineCacheManager caffeineCacheManager = new CaffeineCacheManager();
//        caffeineCacheManager.setCaffeine(caffeine);
//        return caffeineCacheManager;
//    }
//}
