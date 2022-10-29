package venture.blog.pojo;


import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class Article {
    private int id;
    private String title;
    private int cid;
    private String cname;
    private int style;
    private String coverpicture;
    private String description;
    private String maincontent;
    private boolean hidden;
    private boolean recommend;
    private String postdate;
    private String modifydate;
    private int viewsum;
    private int thumbsum;
}
