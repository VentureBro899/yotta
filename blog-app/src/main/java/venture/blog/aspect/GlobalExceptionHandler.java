package venture.blog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import venture.blog.vo.R;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    //指定该方法用于处理Exception异常
    @ExceptionHandler({Exception.class})
    public R defaultHanlder(Exception e) {
        //记得控制台打印错误信息
        log.warn(e.getMessage());
        //返回处理过后的数据给客户端
        return new R("服务器异常，请稍后重试");
    }
}
