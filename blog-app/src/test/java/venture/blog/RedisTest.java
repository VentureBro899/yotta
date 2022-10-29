package venture.blog;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)//Spring测试单元替换原来的要运行期
@ContextConfiguration(value = {"classpath:spring/applicationContext.xml"})
public class RedisTest {
    @Autowired
    StringRedisTemplate redisTemplate;


    @Test
    public void testConnect(){
        redisTemplate.opsForValue().set("test","123");
        System.out.println(redisTemplate.opsForValue().get("jobTotal"));
    }
}
