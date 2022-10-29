package venture.blog.mapper;

import org.apache.ibatis.annotations.Param;
import venture.blog.pojo.Navigator;

import java.util.List;

public interface NavigatorMapper {
    public List<Navigator> findAll();
    public boolean insert(Navigator navigator);
    public boolean del(int id);
    public boolean update(Navigator navigator);
    public Navigator find(int id);
    public List<Navigator> findChildren(@Param(value="pid") int pid);
    public Boolean selectIsExist(int id);
}
