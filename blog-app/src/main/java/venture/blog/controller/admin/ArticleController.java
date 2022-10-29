package venture.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import venture.blog.pojo.Article;
import venture.blog.vo.R;
import venture.blog.service.ArticleService;




@RestController
@RequestMapping("/admin/article")
public class ArticleController {

    @Autowired
    ArticleService articleService;

    @GetMapping({"/list/{size}/{current}/{key}","/list/{size}/{current}"})
    public R getListByPage(@PathVariable int size,@PathVariable int current,@PathVariable(required = false) String key){
        try {
            if(size > 20)
                throw new RuntimeException("每页长度不能大于20");
            PageInfo<Article> pageInfo = articleService.findByPageAndKey(current, size, key);
            return new R(pageInfo);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PostMapping()
    public R addOne(@RequestBody Article article){
        try {
            articleService.insert(article);
            return new R("发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @DeleteMapping()
    public R delSome(@RequestBody int[] ids){
        try {
            articleService.del(ids);
            return new R("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PutMapping()
    public R updOne(@RequestBody Article article){
        try{
            articleService.update(article);
            return new R("更新成功");
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public  R getOne(@PathVariable int id){
        try{
            Article article = articleService.find(id);
            return new R(article);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }


}
