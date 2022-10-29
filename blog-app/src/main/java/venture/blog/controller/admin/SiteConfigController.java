package venture.blog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import venture.blog.pojo.Site;
import venture.blog.service.SiteConfigService;
import venture.blog.vo.R;

@RestController
@RequestMapping("/admin/siteconfig")
public class SiteConfigController {
    @Autowired
    SiteConfigService siteConfigService;

    @PutMapping
    public R updateConfig(@RequestBody Site site) {
        try {
            siteConfigService.update(site);
            return new R(200, "更新成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500, "更新失败");
        }
    }

    @GetMapping
    public R getConfig() {
        Site config = siteConfigService.select();
        if (config != null) {
            return new R(200, "查询成功", config);
        } else {
            return new R(500, "查询错误");
        }

    }
}
