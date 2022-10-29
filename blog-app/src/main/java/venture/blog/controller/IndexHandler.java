package venture.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import venture.blog.dto.RecommendArticle;
import venture.blog.pojo.Article;
import venture.blog.pojo.Navigator;
import venture.blog.pojo.Site;
import venture.blog.pojo.Slider;
import venture.blog.service.ArticleService;
import venture.blog.service.NavigatorService;
import venture.blog.service.SiteConfigService;
import venture.blog.service.SliderService;

import java.util.List;


@Controller
@PropertySource("classpath:global.properties")
public class IndexHandler extends BaseHandler{

    @Autowired
    ArticleService articleService;

    @Autowired
    SliderService sliderService;

    @RequestMapping("/")
    public String index(Model obj) {
        baseInfo(obj);
        // 幻灯片
        List<Slider> sliders = sliderService.findShowing();
        obj.addAttribute("sliders",sliders);
        // 推荐
        List<RecommendArticle> recommends = articleService.findRecommendArticle();
        obj.addAttribute("recommends",recommends);
        // 文章
        List<Article> articles = articleService.findByPageAndKey(1, 10, null).getList();
        obj.addAttribute("articles", articles);
        return "index";
    }

}
