package venture.blog.service;

import com.github.pagehelper.PageInfo;
import venture.blog.pojo.Category;

public interface CategoryService {
    public PageInfo<Category> findByPageAndKey(int current, int size, String key);
    public boolean insert(Category category);
    public void del(int[] ids);
    public boolean update(Category category);
    public Category find(int id);
}
