package venture.blog.service;

import com.github.pagehelper.PageInfo;
import venture.blog.dto.HotArticle;
import venture.blog.dto.RecommendArticle;
import venture.blog.pojo.Article;

import java.util.List;
import java.util.Map;

public interface ArticleService {
    PageInfo<Article> findByPageAndKey(int current, int size, String key);
    PageInfo<Article> findByCate(int current, int size, int cid);
    boolean insert(Article article);
    void del(int[] ids);
    boolean update(Article article);
    Article find(int id);
    List<RecommendArticle> findRecommendArticle();
    /**
     * @param id
     * @param flag
     *  true 点赞
     *  false 取消赞
     * @return boolean
     * @author venture
     * @creed: Nothing Ventured,nothing gained
     * @date 2022/10/2 15:11
     */

    void updateThumbSum(int id, boolean flag);


    // ↓统计所有文章数据
    // 文章总数
    int selectCount();
    // 点赞总数
    int selectThumbCount();
    // 浏览总数
    int selectViewCount();
    // 最近某天发布的文章数
    Map selectBefore7DayCount();

    // 热榜文章
    List<HotArticle> selectHot();
}
