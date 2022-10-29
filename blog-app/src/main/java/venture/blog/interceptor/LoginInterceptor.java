package venture.blog.interceptor;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import org.springframework.web.servlet.HandlerInterceptor;
import venture.blog.utils.JWTUtil;
import venture.blog.vo.R;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("X-Token");
        if(StrUtil.isBlank(token) || !JWTUtil.verify(token)){
            String body = JSONUtil.toJsonStr(new R(50008, "未登录，请先登录"));
            response.setStatus(401);
            response.setContentType("Application/json;charset=utf-8");
            response.getOutputStream().write(body.getBytes("utf-8"));
            return false;
        }
        return true;
    }
}
