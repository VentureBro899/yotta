package venture.blog.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import venture.blog.mapper.SiteConfigMapper;
import venture.blog.pojo.Site;
import venture.blog.service.SiteConfigService;
import venture.blog.utils.RedisConstants;


@Service
public class SiteConfigServiceImpl implements SiteConfigService {
    @Autowired
    SiteConfigMapper siteConfigMapper;

    @Autowired
    RedisTemplate redisTemplate;

    @Override
    public boolean update(Site site) {
        if(siteConfigMapper.update(site)){//同步更新缓存
            redisTemplate.opsForValue().set(RedisConstants.SITE_CONFIG_KEY,site);
        }
        return true;
    }

    @Override
    public Site select() {
        // 网站信息变更不频繁，所以缓存到redis
        Site blogSiteConfig = (Site)redisTemplate.opsForValue().get(RedisConstants.SITE_CONFIG_KEY);
        if(blogSiteConfig ==null){
            blogSiteConfig = siteConfigMapper.select();
            redisTemplate.opsForValue().set(RedisConstants.SITE_CONFIG_KEY, blogSiteConfig);
        }
        return blogSiteConfig;
    }
}
