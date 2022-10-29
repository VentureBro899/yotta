package venture.blog.service;

import venture.blog.pojo.Site;

public interface SiteConfigService {
    // 更新配置
    boolean update(Site site);
    // 查询所有配置
    Site select();
}
