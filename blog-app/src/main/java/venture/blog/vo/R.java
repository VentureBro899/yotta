package venture.blog.vo;
import lombok.Data;

//Http Reponse体返回对象
@Data
public class R {
    private int code;
    private String msg;
    private Object data;

    public R() {
        this.code = 200;
        this.msg = "success";
    }

    public R(String msg) {
        this.code = 200;
        this.msg = msg;
    }

    public R(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(Object data) {
        this.code = 200;
        this.msg = "got data successfully";
        this.data = data;
    }

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
