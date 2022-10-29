package venture.blog.dto;

import lombok.Data;

@Data
public class AdminInfo {
    private String admin_name;
    private String admin_newName;
    private String admin_pwd;
    private String admin_newPwd;
}
