package venture.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import venture.blog.exception.DataIntegrityException;
import venture.blog.mapper.CategoryMapper;
import venture.blog.mapper.NavigatorMapper;
import venture.blog.pojo.Category;
import venture.blog.pojo.Navigator;
import venture.blog.service.NavigatorService;
import venture.blog.utils.RedisConstants;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class NavigatorServiceImpl implements NavigatorService {
    @Autowired
    NavigatorMapper navigatorMapper;


    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Override
    public List<Navigator> findAll() {
        ValueOperations<String, String> op = stringRedisTemplate.opsForValue();
        String jsonstr = op.get(RedisConstants.NAVIGATORS_KEY);
        if (jsonstr != null){
            return JSONUtil.toList(jsonstr, Navigator.class);
        }
        List<Navigator> list = navigatorMapper.findAll();
        op.set(RedisConstants.NAVIGATORS_KEY,JSONUtil.toJsonStr(list));
        return list;
    }

    @Override
    public boolean insert(Navigator navigator) {
        //完整性约束，分类id存在才能指定
        int nav_id = navigator.getCid();
        int parent_id = navigator.getPid();
        if(nav_id != 0){
            Category category = categoryMapper.find(nav_id);
            if(category == null) throw new DataIntegrityException("所指定分类不存在");
        }

        //完整性约束，父类导航存在才能成功添加
        //父类等于0说明其没有父类，无需验证
        if(parent_id != 0){
            Navigator nav = navigatorMapper.find(parent_id);
            if(nav == null) throw new DataIntegrityException("父导航不存在");
        }
        if (navigator.getRoute().length() == 0){
            navigator.setRoute(null);
        }
        return navigatorMapper.insert(navigator);
    }

    @Override
    public void del(int[] ids) {
        for (int id : ids) {
            if(navigatorMapper.selectIsExist(id)){
                throw new DataIntegrityException("所选导航存在子导航，请先删除子导航");
            }
            navigatorMapper.del(id);
        }
    }

    @Override
    public boolean update(Navigator navigator) {
        //完整性约束，分类id存在才能指定
        int nav_id = navigator.getCid();
        int parent_id = navigator.getPid();
        if(nav_id != 0){
            Category category = categoryMapper.find(nav_id);
            if(category == null) throw new DataIntegrityException("所指定分类不存在");
        }

        //完整性约束，父类导航存在才能成功添加
        //父类等于0说明其没有父类，无需验证
        if(parent_id != 0){
            Navigator nav = navigatorMapper.find(parent_id);
            if(nav == null) throw new DataIntegrityException("父导航不存在");
        }
        if (navigator.getRoute() == null || navigator.getRoute().length() == 0){
            navigator.setRoute(null);
        }
        return navigatorMapper.update(navigator);
    }

    @Override
    public Navigator find(int id) {
        return navigatorMapper.find(id);
    }
}
