package venture.blog.controller;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import venture.blog.pojo.Article;
import venture.blog.service.ArticleService;

@Controller
@RequestMapping("/search")
public class SearchHandler extends BaseHandler{
    @Autowired
    ArticleService articleService;
    @RequestMapping({"/{size}/{current}/{key}"})
    public String search(@PathVariable String key, @PathVariable int size, @PathVariable int current, Model model){
        baseInfo(model,"搜索:" + key);
        PageInfo<Article> page = articleService.findByPageAndKey(current, size, key);
        model.addAttribute("pageInfo",page);
        int[] pageSum = new int[page.getPages()];
        for (int i = 0;i < pageSum.length;i++){
            pageSum[i] = i + 1;
        }
        model.addAttribute("pageSum",pageSum);
        model.addAttribute("keyword",key);
        return "search";
    }
}
