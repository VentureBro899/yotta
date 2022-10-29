package venture.blog.service;


import venture.blog.dto.AdminInfo;

public interface AuthService {
    // 验证账号密码
    public boolean VerifyPwd(AdminInfo adminInfo);
    public boolean updateUserAndPwd(AdminInfo adminInfo);
}
