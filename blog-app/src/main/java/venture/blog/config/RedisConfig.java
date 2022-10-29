package venture.blog.config;

import com.alibaba.fastjson.support.spring.FastJsonRedisSerializer;
import com.alibaba.fastjson.support.spring.GenericFastJsonRedisSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;
import venture.blog.pojo.Site;

@Configuration
public class RedisConfig {
    @Value("${redis.host}")
    private String hostname;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.password}")
    private String password;

    @Value("${redis.timeout}")
    private int timeout;

    @Value("${redis.database}")
    private int database;

    @Value("${redis.maxIdle}")
    private int maxIdle;

    @Value("${redis.maxTotal}")
    private int maxTotal;

    private RedisConnectionFactory factory = null;

    @Bean("redisConnectionFactory")
    public RedisConnectionFactory initConnectionFactory() {
        if (factory != null) {
            return factory;
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        // 最大空闲数
        poolConfig.setMaxIdle(maxIdle);
        // 最大连接数
        poolConfig.setMaxTotal(maxTotal);
        // 最大等待毫秒数
        poolConfig.setMaxWaitMillis(timeout);
        // 创建连接工厂
        JedisConnectionFactory jedisConnectionFactory = new JedisConnectionFactory(poolConfig);
        // 获取单机的Redis配置
        RedisStandaloneConfiguration rsConfig = jedisConnectionFactory.getStandaloneConfiguration();
        rsConfig.setHostName(hostname);
        rsConfig.setPort(port);
        rsConfig.setPassword(password);
        rsConfig.setDatabase(database);
        factory = jedisConnectionFactory;
        return factory;
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> initRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        GenericFastJsonRedisSerializer genericFastJsonRedisSerializer = new GenericFastJsonRedisSerializer();
        template.setConnectionFactory(factory);
        // key的序列化采用StringRedisSerializer
        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        // value对的序列化采用fastJsonRedisSerializer
        template.setValueSerializer(genericFastJsonRedisSerializer);
        template.setHashValueSerializer(genericFastJsonRedisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Bean("stringRedisTemplate")
    public StringRedisTemplate stringRedisTemplate(RedisConnectionFactory connectionFactory) {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(connectionFactory);
        return stringRedisTemplate;
    }
}
