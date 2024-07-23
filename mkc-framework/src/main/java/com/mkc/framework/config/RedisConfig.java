package com.mkc.framework.config;

import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.CacheProperties.Redis;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;

/**
 * redis配置
 * 
 * @author atd
 */
@Configuration
@EnableCaching
@EnableConfigurationProperties(CacheProperties.class)
public class RedisConfig extends CachingConfigurerSupport {
	
	@Bean
	public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<Object, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//
//        FastJson2JsonRedisSerializer serializer = new FastJson2JsonRedisSerializer(Object.class);
//
//        ObjectMapper mapper = new ObjectMapper();
//        mapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        mapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL, JsonTypeInfo.As.PROPERTY);
//        serializer.setObjectMapper(mapper);
//
//        template.setValueSerializer(serializer);
//        // 使用StringRedisSerializer来序列化和反序列化redis的key值
//        template.setKeySerializer(new StringRedisSerializer());
//        template.afterPropertiesSet();
//        return template;

		RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
		redisTemplate.setConnectionFactory(connectionFactory);

		redisTemplate.setKeySerializer(new StringRedisSerializer());
		redisTemplate.setValueSerializer(new GenericFastJsonRedisSerializer());
		redisTemplate.setConnectionFactory(connectionFactory);
		
		return redisTemplate;
	}
	
	@Bean
	public RedisCacheManager cacheManager(CacheProperties cacheProperties, RedisConnectionFactory redisConnectionFactory) {
		Redis redisProperties = cacheProperties.getRedis();
		
		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
				// RedisSerializer.json()
				.serializeValuesWith(SerializationPair.fromSerializer(new GenericFastJsonRedisSerializer()));
		redisCacheConfiguration = redisCacheConfiguration.computePrefixWith(name -> name.endsWith(":") ? name : name + ":");
		
		if (redisProperties.getTimeToLive() != null) {
			redisCacheConfiguration = redisCacheConfiguration.entryTtl(redisProperties.getTimeToLive());
		}
		if (redisProperties.getKeyPrefix() != null) {
			redisCacheConfiguration = redisCacheConfiguration.prefixCacheNameWith(redisProperties.getKeyPrefix());
		}
		if (!redisProperties.isCacheNullValues()) {
			redisCacheConfiguration = redisCacheConfiguration.disableCachingNullValues();
		}
		if (!redisProperties.isUseKeyPrefix()) {
			redisCacheConfiguration = redisCacheConfiguration.disableKeyPrefix();
		}
		
		return RedisCacheManager.builder(redisConnectionFactory).cacheDefaults(redisCacheConfiguration).build();
	}
}
