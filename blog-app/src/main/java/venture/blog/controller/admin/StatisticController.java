package venture.blog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import venture.blog.service.ArticleService;
import venture.blog.vo.R;

import java.util.Map;

@RestController
@RequestMapping("/admin/statistic")
public class StatisticController {
    @Autowired
    ArticleService articleService;
    @GetMapping("/articleCount")
    public R getArticleCount(){
        try{
            int count = articleService.selectCount();
            return new R(count);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @GetMapping("/viewCount")
    public R getViewCount(){
        try{
            int count = articleService.selectViewCount();
            return new R(count);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }
    @GetMapping("/thumbCount")
    public R getThumbCount(){
        try{
            int count = articleService.selectThumbCount();
            return new R(count);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }
    @GetMapping("/articleTrend")
    public R getArticleTrendBefore7Day(){
        try{
            Map count = articleService.selectBefore7DayCount();
            return new R(count);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }
}
