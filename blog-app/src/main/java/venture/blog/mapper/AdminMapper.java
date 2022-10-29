package venture.blog.mapper;

import venture.blog.dto.AdminInfo;
import venture.blog.pojo.Admin;

public interface AdminMapper {
    public boolean selectByPwd(Admin admin);
    public boolean update(Admin admin);
}
