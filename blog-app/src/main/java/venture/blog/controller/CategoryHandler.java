package venture.blog.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import venture.blog.pojo.Article;
import venture.blog.pojo.Category;
import venture.blog.service.ArticleService;
import venture.blog.service.CategoryService;

@Controller
@RequestMapping("/list")
public class CategoryHandler extends BaseHandler{
    @Autowired
    ArticleService articleService;

    @Autowired
    CategoryService categoryService;

    @GetMapping("/{cid}/{size}/{current}")
    public String List(@PathVariable int size, @PathVariable int current, @PathVariable int cid, Model model){
        Category category = categoryService.find(cid);
        baseInfo(model,category.getName());
        model.addAttribute(category);
        PageInfo<Article> page = articleService.findByCate(current, size, cid);
        model.addAttribute("pageInfo",page);
        int[] pageSum = new int[page.getPages()];
        for (int i = 0;i < pageSum.length;i++){
            pageSum[i] = i + 1;
        }
        model.addAttribute("pageSum",pageSum);
        return "category";
    }
}
