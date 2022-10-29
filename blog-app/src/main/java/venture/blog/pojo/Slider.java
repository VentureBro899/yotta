package venture.blog.pojo;

import lombok.Data;

@Data
public class Slider {
    private int id;
    private String title;
    private boolean showing;
    private String picture;
    private String bgcolor;
    private int reorder;
    private String route;
    private String postdate;
}
