package venture.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ui.Model;
import venture.blog.dto.HotArticle;
import venture.blog.pojo.Navigator;
import venture.blog.pojo.Site;
import venture.blog.service.ArticleService;
import venture.blog.service.NavigatorService;
import venture.blog.service.SiteConfigService;

import java.util.HashMap;
import java.util.List;

public class BaseHandler {
    @Autowired
    SiteConfigService siteConfigService;
    @Autowired
    NavigatorService navigatorService;
    @Autowired
    ArticleService articleService;
    @Value("${picture.predomain}")
    private String imgdomain;
    public void baseInfo(Model obj,String... title){
        obj.addAttribute("imgdomain", imgdomain);
        // 导航
        List<Navigator> navigators = navigatorService.findAll();
        obj.addAttribute("navigators",navigators);
        // 统计数据
        HashMap<String, Integer> statistic = new HashMap<>();
        int articleCount = articleService.selectCount();
        statistic.put("articleCount",articleCount);
        int thumbCount = articleService.selectThumbCount();
        statistic.put("thumbCount",thumbCount);
        int viewCount = articleService.selectViewCount();
        statistic.put("viewCount",viewCount);
        obj.addAttribute("statistic",statistic);
        // 热门文章
        List<HotArticle> hotArticles = articleService.selectHot();
        obj.addAttribute("hotArticles",hotArticles);
        // 博客信息
        Site site = siteConfigService.select();
        if(title.length == 1){// 如果传入新标题，则将新标题作为主标题
            site.setSite_subtitle(site.getSite_title());
            site.setSite_title(title[0]);
        }
        obj.addAttribute("site",site);
    }
}
