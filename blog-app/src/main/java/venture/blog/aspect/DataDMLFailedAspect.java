package venture.blog.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import venture.blog.exception.EffectZeroROwException;

@Aspect
@Component
public class DataDMLFailedAspect {
  /*  @Pointcut("execution(boolean venture.blog.service.impl.ArticleServiceImpl.*(..)) ||" +
            "execution(boolean venture.blog.service.impl.CategoryServiceImpl.*(..)) ||" +
            "execution(boolean venture.blog.service.impl.NavigatorServiceImpl.*(..))")
    public void pointCut() {
    }

    // 插入修改影响行数为0时，进行异常抛出
    @Around("pointCut()")
    public boolean serviceNotice(ProceedingJoinPoint pjp) throws Throwable {
        boolean proceed = (boolean) pjp.proceed();
        if (!proceed) throw new EffectZeroROwException("参数不合法");
        return true;
    }*/
}
