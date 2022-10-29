package venture.blog.mapper;

import org.apache.ibatis.annotations.Param;
import venture.blog.dto.RecommendArticle;
import venture.blog.pojo.Article;

import java.util.List;

public interface ArticleMapper {
    List<Article> findAll(@Param(value="key") String key);
    List<Article> findByCate(@Param(value="cid") int cid);
    boolean insert(Article article);
    boolean del(int id);
    boolean update(Article article);
    Article find(int id);
    List<RecommendArticle> findRecommendList();
    // 查询指定分类的文章是否存在
    Boolean selectIsExist(int cid);
    int selectThumbSum(int id);
    boolean addThumbSum(int id);
    boolean subThumbSum(int id);
    int selectCount();
    int selectThumbCount();
    int selectViewCount();
    int selectSomeDayCount(String nowdate);

    public List<Article> selectHot();
}
