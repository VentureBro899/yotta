package venture.blog.service;

import venture.blog.pojo.Navigator;

import java.util.List;

public interface NavigatorService {
    public List<Navigator> findAll();
    public boolean insert(Navigator navigator);
    public void del(int[] ids);
    public boolean update(Navigator navigator);
    public Navigator find(int id);
}
