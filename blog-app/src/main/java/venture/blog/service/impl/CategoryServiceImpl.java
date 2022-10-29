package venture.blog.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import venture.blog.exception.DataIntegrityException;
import venture.blog.mapper.ArticleMapper;
import venture.blog.mapper.CategoryMapper;
import venture.blog.pojo.Category;
import venture.blog.service.CategoryService;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    ArticleMapper articleMapper;

    @Override
    public PageInfo<Category> findByPageAndKey(int current, int size, String key) {
        PageHelper.startPage(current,size);
        List<Category> list = categoryMapper.findAll(key);
        PageInfo<Category> pi = new PageInfo<>(list);
        return pi;
    }

    @Override
    public boolean insert(Category category) {
        return categoryMapper.insert(category);
    }

    @Override
    @Transactional
    public void del(int[] ids) {
        for (int id : ids) {
            if(articleMapper.selectIsExist(id)){
                throw new DataIntegrityException("所选分类" + id +"下存在文章，禁止删除!");
            }
            categoryMapper.del(id);
        }
    }

    @Override
    public boolean update(Category category) {
        return categoryMapper.update(category);
    }

    @Override
    public Category find(int id) {
        return categoryMapper.find(id);
    }
}
