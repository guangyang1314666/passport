package com.guangyang.development.config;

import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author 沈光阳
 * @create 2019-12-25-19:59
 */
@Configuration
public class RedissonConfig {
    public static final Logger logger = LoggerFactory.getLogger(RedissonConfig.class);

    @Value("${spring.redis.host:0}")
    private String host;

    @Value("${spring.redis.port:0}")
    private String port;

    @Bean
    public RedissonClient redissonClient(){

        if (host.equals("0") && port.equals("0")) {
            logger.error("没有配置缓存的主机ip以及端口号");
            return null;
        }

        Config config = new Config();
        config.useSingleServer().setAddress("redis://"+host+":"+port);
        RedissonClient redisson = Redisson.create(config);
        return redisson;
    }
}
