package venture.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import venture.blog.pojo.Article;
import venture.blog.service.ArticleService;
import venture.blog.vo.R;

import java.util.List;

// 前台文章详情

@Controller
@RequestMapping("/article")
public class ArticleHandler extends BaseHandler {
    @Autowired
    ArticleService articleService;

    @RequestMapping("/{id}")
    public String getOne(@PathVariable int id, Model model){
        baseInfo(model);
        Article article = articleService.find(id);
        model.addAttribute(article);
        return "article";
    }


    @ResponseBody
    @PostMapping("/thumbup/{id}")
    public R updThumbSum(boolean flag,@PathVariable int id){
        try{
            //articleService.updateThumbSumAtRedis(id,(boolean)map.get("flag"));
            articleService.updateThumbSum(id,flag);
            return new R(200,"点赞成功");
        }catch(Exception e){
            e.printStackTrace();
            return new R(500);
        }
    }
}
