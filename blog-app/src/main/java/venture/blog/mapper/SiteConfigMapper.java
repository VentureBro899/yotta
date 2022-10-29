package venture.blog.mapper;

import venture.blog.pojo.Site;

public interface SiteConfigMapper {
    public boolean update(Site site);

    public Site select();


}
