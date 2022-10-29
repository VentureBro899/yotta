package venture.blog.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import venture.blog.dto.AdminInfo;
import venture.blog.mapper.AdminMapper;
import venture.blog.pojo.Admin;
import venture.blog.service.AuthService;
import venture.blog.utils.CMd5;

@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    AdminMapper adminMapper;

    @Override
    public boolean VerifyPwd(AdminInfo adminInfo) {
        Admin admin = new Admin(adminInfo.getAdmin_name(),CMd5.encrypt(adminInfo.getAdmin_pwd()));
        return adminMapper.selectByPwd(admin);
    }

    @Override
    public boolean updateUserAndPwd(AdminInfo adminInfo) {
        Admin admin = new Admin(adminInfo.getAdmin_newName(),CMd5.encrypt(adminInfo.getAdmin_newPwd()));
        return adminMapper.update(admin);
    }


}
