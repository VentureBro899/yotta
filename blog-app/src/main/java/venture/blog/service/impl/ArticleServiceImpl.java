package venture.blog.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import venture.blog.dto.HotArticle;
import venture.blog.dto.RecommendArticle;
import venture.blog.exception.DataIntegrityException;
import venture.blog.mapper.ArticleMapper;
import venture.blog.mapper.CategoryMapper;
import venture.blog.pojo.Article;
import venture.blog.service.ArticleService;
import venture.blog.utils.RedisConstants;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    ArticleMapper articleMapper;

    @Autowired
    CategoryMapper categoryMapper;

    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Autowired
    RedisTemplate redisTemplate;


    @Override
    public PageInfo<Article> findByPageAndKey(int current, int size, String key) {
        PageHelper.startPage(current,size);
        List<Article> list = articleMapper.findAll(key);
        PageInfo<Article> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public PageInfo<Article> findByCate(int current, int size, int cid) {
        PageHelper.startPage(current,size);
        List<Article> list = articleMapper.findByCate(cid);
        PageInfo<Article> pageInfo = new PageInfo<>(list);
        return pageInfo;
    }

    @Override
    public boolean insert(Article article) {
        int cid = article.getCid();
       if(categoryMapper.find(cid) == null){
           throw new DataIntegrityException("分类不存在");
       }
        return articleMapper.insert(article);
    }

    @Override
    public void del(int[] ids) {
        for (int id : ids) {
            articleMapper.del(id);
        }
    }

    @Override
    public boolean update(Article article) {
        int cid = article.getCid();
        if(categoryMapper.find(cid) == null){
            throw new DataIntegrityException("指定分类不存在");
        }
        //更新时间
        String modifyDate = DateUtil.format(new Date(), "yyyy-MM-dd HH:mm:ss");
        article.setModifydate(modifyDate);
        return articleMapper.update(article);
    }

    @Override
    public Article find(int id) {
        return articleMapper.find(id);
    }

    @Override
    public List<RecommendArticle> findRecommendArticle() {
        return articleMapper.findRecommendList();
    }

    @Override
    public void updateThumbSum(int id, boolean flag) {
        if (flag){
            articleMapper.addThumbSum(id);
        }else {
            articleMapper.subThumbSum(id);
        }
    }


    @Override
    public int selectCount() {
        String articleCount = stringRedisTemplate.opsForValue().get(RedisConstants.STATISTIC_ARTICLECOUNT_KEY);
        if(articleCount != null){
            return Integer.parseInt(articleCount);
        }
        else{
            articleCount = String.valueOf(articleMapper.selectCount());
            stringRedisTemplate.opsForValue().set(RedisConstants.STATISTIC_ARTICLECOUNT_KEY,articleCount,1L, TimeUnit.DAYS);
            return Integer.parseInt(articleCount);
        }
    }

    @Override
    public int selectThumbCount() {
        String thumbCount = stringRedisTemplate.opsForValue().get(RedisConstants.STATISTIC_THUMBCOUNT_KEY);
        if(thumbCount != null){
            return Integer.parseInt(thumbCount);
        }
        else{
            thumbCount = String.valueOf(articleMapper.selectThumbCount());
            stringRedisTemplate.opsForValue().set(RedisConstants.STATISTIC_THUMBCOUNT_KEY,thumbCount,1L, TimeUnit.DAYS);
            return Integer.parseInt(thumbCount);
        }
    }

    @Override
    public int selectViewCount() {
        String viewCount = stringRedisTemplate.opsForValue().get(RedisConstants.STATISTIC_VIEWCOUNT_KEY);
        if(viewCount != null){
            return Integer.parseInt(viewCount);
        }
        else{
            viewCount = String.valueOf(articleMapper.selectViewCount());
            stringRedisTemplate.opsForValue().set(RedisConstants.STATISTIC_VIEWCOUNT_KEY,viewCount,1L, TimeUnit.DAYS);
            return Integer.parseInt(viewCount);
        }
    }

    @Override
    public Map selectBefore7DayCount() {
        HashMap<String, Integer> recent = new HashMap<>();
        for (int i = 0;i < 7;i++){
            Calendar ca = Calendar.getInstance();
            ca.add(Calendar.DAY_OF_MONTH,-i);
            int count = articleMapper.selectSomeDayCount(DateUtil.format(ca.getTime(), "yyyy-MM-dd"));
            recent.put(DateUtil.format(ca.getTime(),"MM-dd"),count);
        }
        return recent;
    }

    @Override
    public List<HotArticle> selectHot() {
        List<HotArticle> hotList = (List<HotArticle>) redisTemplate.opsForValue().get(RedisConstants.HOT_ARTICLES_KEY);
        if(hotList == null){
            List<HotArticle> hotArticles = articleMapper.selectHot().stream().map(item -> BeanUtil.copyProperties(item, HotArticle.class)).collect(Collectors.toList());
            redisTemplate.opsForValue().set(RedisConstants.HOT_ARTICLES_KEY,hotArticles,1L,TimeUnit.DAYS);
            hotList = hotArticles;
        }
        return hotList;
    }
}
