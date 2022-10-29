package venture.blog.aspect;


import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import venture.blog.utils.RedisConstants;

@Aspect
@Component
public class RedisDelCacheAspect {
    @Pointcut("execution(* venture.blog.service.impl.NavigatorServiceImpl.*(..)) && " +
            "!execution(* venture.blog.service.impl.NavigatorServiceImpl.findAll())")
    public void pointCutOfNav(){}

    @Pointcut("execution(* venture.blog.service.impl.SliderServiceImpl.update(venture.blog.pojo.Slider)) || " +
            "execution(* venture.blog.service.impl.SliderServiceImpl.insert(venture.blog.pojo.Slider)) || "  +
            "execution(* venture.blog.service.impl.SliderServiceImpl.del(..))")
    public void pointCutOfSdr(){}
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    // 更新、修改、删除导航后更新缓存
    @After("pointCutOfNav()")
    public void delNavCache(){
        stringRedisTemplate.delete(RedisConstants.NAVIGATORS_KEY);
    }
    // 更新、修改、删除幻灯片后更新缓存
    @After("pointCutOfSdr()")
    public void delSdrCache(){
        stringRedisTemplate.delete(RedisConstants.SLIDERS_KEY);
    }
}
