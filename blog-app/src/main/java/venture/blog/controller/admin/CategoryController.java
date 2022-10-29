package venture.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import venture.blog.pojo.Category;
import venture.blog.service.CategoryService;
import venture.blog.vo.R;

@RestController
@RequestMapping("/admin/cate")
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping({"/list/{size}/{current}","/list/{size}/{current}/{key}"})
    public R getList(@PathVariable int size,@PathVariable int current,@PathVariable(required = false) String key){
        try {
            PageInfo<Category> pageInfo = categoryService.findByPageAndKey(current, size, key);
            return new R(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PostMapping()
    public R addOne(@RequestBody Category category){
        try {
            categoryService.insert(category);
            return new R();
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @DeleteMapping()
    public R delSome(@RequestBody int[] ids){
        try {
            categoryService.del(ids);
            return new R();
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PutMapping()
    public R updOne(@RequestBody Category category){
        try{
            categoryService.update(category);
            return new R();
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public  R getOne(@PathVariable int id){
        try{
            Category category = categoryService.find(id);
            return new R(category);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }
}
