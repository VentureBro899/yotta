package venture.blog.controller.admin;

import cn.hutool.core.lang.UUID;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import venture.blog.dto.AdminInfo;
import venture.blog.service.AuthService;
import venture.blog.utils.JWTUtil;
import venture.blog.vo.R;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/admin/auth")
@PropertySource("classpath:global.properties")
public class AuthController {
    @Value("${qiniu.accessKey}")
    private String accessKey;
    @Value("${qiniu.secretKey}")
    private String secretKey;
    @Value("${qiniu.bucket}")
    private String bucket;
    @Value("${qiniu.region}")
    private String area;

    @Value("${picture.predomain}")
    private String predomain;


    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    AuthService authService;


    @PostMapping("/login")
    public R login(@RequestBody AdminInfo adminInfo) {
        boolean flag = authService.VerifyPwd(adminInfo);
        Map<String, String> loginInfo = new HashMap<>();
        if (flag) {
            //验证通过，生成JWT
            String uuid = UUID.randomUUID().toString();
            // 指定登录过期时间为2天
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, 2);
            loginInfo.put("login_name", adminInfo.getAdmin_name());
            loginInfo.put("uuid", uuid);
            String token = JWTUtil.getToken(loginInfo);
            HashMap<String, String> tokenmap = new HashMap<>();
            tokenmap.put("token", token);
            return new R(200, "登录成功", tokenmap);
        } else {
            return new R(500, "用户名或密码错误！");
        }
    }

    @GetMapping("/logininfo")
    public R loginInfo(@RequestHeader(value = "X-Token", required = true) String token) {
        HashMap<String, String> map = new HashMap<>();
        if (JWTUtil.verify(token)) {
            map.put("avatar", "https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif");
            map.put("name", JWTUtil.decode(token).getClaims().get("login_name").asString());
            map.put("imgdomain", predomain);
            return new R(200, "", map);
        } else {
            return new R(50008);
        }
    }

    @PostMapping("/logout")
    public R logout(@RequestParam String token) {
        DecodedJWT decode = JWTUtil.decode(token);
        return new R(200, "退出成功");
    }

    @GetMapping("/uploadtoken")
    public R upload(@RequestHeader(value = "X-Token", required = true) String token) {
        HashMap<String, String> data = new HashMap<>();
        if (JWTUtil.verify(token)) {
            // 1.七牛云认证
            Auth auth = Auth.create(accessKey, secretKey);
            //2..取得上传token
            String upToken = auth.uploadToken(bucket, null, 60 * 60 * 24 * 2, null);
            data.put("area", area);
            data.put("uptoken", upToken);
            return new R(200, "ok", data);
        }
        return new R(50008, "");
    }

    @PutMapping("/updateAdmin")
    public R updateAdmin(@RequestBody AdminInfo adminInfo,@RequestHeader(value = "X-Token", required = true) String token){
        try{
            adminInfo.setAdmin_name(JWTUtil.decode(token).getClaim("login_name").asString());
            if(!authService.VerifyPwd(adminInfo)){
                return new R(500,"原密码验证错误");
            }
            System.out.println(JWTUtil.decode(token).getClaim("login_name").toString());
            authService.updateUserAndPwd(adminInfo);
            return new R(200);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }
}
