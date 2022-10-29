package venture.blog.pojo;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@NoArgsConstructor
@Data
public class Category {
    private int id;
    private String name;
    private String detail;
    private int style;
}
