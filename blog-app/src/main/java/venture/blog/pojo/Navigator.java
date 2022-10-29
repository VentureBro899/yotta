package venture.blog.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@Data
public class Navigator {
    private int id;
    private String title;
    private int pid;
    private String route;
    private int cid;
    private String icon;
    private List<Navigator> children;
}
