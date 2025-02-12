package com.mkc.util;

import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.mkc.common.core.redis.RedisCache;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * @author chentong
 * @version 1.0
 * @description: description
 * @date 2025/2/12 13:34
 */
public class RateLimitUtil {

	public static boolean rateLimit(String productCode, String merCode, Long reqLimit) {
		if (Objects.isNull(reqLimit) || reqLimit == 0) {
			return false;
		}
		RedisCache redisCache = SpringUtil.getBean(RedisCache.class);
		String key = CharSequenceUtil.format("RATE_LIMIT_{}_{}", productCode, merCode);
		Long requestedTimes = redisCache.getCacheObject(key);

		if (Objects.isNull(requestedTimes)) {

			LocalDateTime now = LocalDateTime.now();
			LocalDateTime endOfDay = LocalDateTime.of(now.toLocalDate(), LocalTime.MAX);
			Duration duration = Duration.between(now, endOfDay);
			long milliseconds = duration.toMillis();
			redisCache.setCacheObject(key, 1L, milliseconds, TimeUnit.MILLISECONDS);
			return false;
		} else if (requestedTimes < reqLimit) {
			redisCache.setCacheObject(key, requestedTimes + 1);
			return false;
		}
		return true;

	}

}
