package venture.blog.pojo;


import lombok.Data;

import java.io.Serializable;

@Data
public class Site {
    private String site_title;
    private String site_subtitle;
    private String site_keywords;
    private String site_description;
    private String site_logo;
    private String site_owner;
    private String site_owner_avatar;
    private String site_owner_motto;
}
