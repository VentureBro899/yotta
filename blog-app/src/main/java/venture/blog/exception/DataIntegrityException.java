package venture.blog.exception;

/**
 * 
 * 数据完整性异常 
 *用于约束文章所属分类，导航指向分类不能为空等
 * @return 
 * @author venture
 * @creed: Nothing Ventured,nothing gained
 * @date 2022/9/2 21:59
 */

public class DataIntegrityException extends RuntimeException{
    public DataIntegrityException(){
        super("完整性约束异常");
    }
    public DataIntegrityException(String msg){
        super(msg);
    }
}
