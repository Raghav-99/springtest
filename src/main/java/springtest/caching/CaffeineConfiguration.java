package springtest.caching;

import java.time.Duration;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.github.benmanes.caffeine.cache.Caffeine;

@Configuration
@EnableCaching
public class CaffeineConfiguration implements CachingConfigurer {
	//@Bean
	public com.github.benmanes.caffeine.cache.Caffeine<Object, Object> configure() {
		return Caffeine.newBuilder().expireAfterAccess(Duration.ofMillis(3000)).evictionListener((k, v, reason) -> {
			System.out.printf("WARN: Entry evicted- %s:%s:%s\n", k, v, reason.name());
		});
	}
	
	@Bean
	@Override
	public CacheManager cacheManager() {
		CaffeineCacheManager manager = new CaffeineCacheManager("message");
		manager.setCaffeine(configure());
		return manager;
	}
}
