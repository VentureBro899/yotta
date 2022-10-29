package venture.blog.mapper;

import org.apache.ibatis.annotations.Param;
import venture.blog.pojo.Category;

import java.util.List;

public interface CategoryMapper {
    public List<Category> findAll(@Param(value="key")String key);
    public boolean insert(Category category);
    public boolean del(int id);
    public boolean update(Category category);
    public Category find(int id);
    public String findCname(int id);
}
