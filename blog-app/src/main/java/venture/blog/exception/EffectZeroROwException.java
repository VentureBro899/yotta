package venture.blog.exception;

/**
 * 影响函数为0异常
 * update、delete不存在的数据时抛出此异常
 * @return
 * @author venture
 * @creed: Nothing Ventured,nothing gained
 * @date 2022/9/2 22:33
 */

public class EffectZeroROwException extends RuntimeException{
    public EffectZeroROwException() {
        super("影响函数为0异常");
    }

    public EffectZeroROwException(String msg){
        super(msg);
    }
}
