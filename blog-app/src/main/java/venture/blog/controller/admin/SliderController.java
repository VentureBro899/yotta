package venture.blog.controller.admin;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import venture.blog.pojo.Slider;
import venture.blog.service.SliderService;
import venture.blog.vo.R;

@RestController
@RequestMapping("/admin/slider")
public class SliderController {
    @Autowired
    SliderService sliderService;

    @GetMapping({"/list/{size}/{current}/{key}","/list/{size}/{current}"})
    public R list(@PathVariable int size,@PathVariable int current,@PathVariable(required = false) String key){
        try{
            PageInfo<Slider> page = sliderService.findByPageAndKey(size, current, key);
            return new R(page);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PostMapping()
    public R addOne(@RequestBody Slider slider){
        try {
            sliderService.insert(slider);
            return new R("发布成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @DeleteMapping()
    public R delSome(@RequestBody int[] ids){
        try {
            sliderService.del(ids);
            return new R("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PutMapping()
    public R updOne(@RequestBody Slider slider){
        try{
            sliderService.update(slider);
            return new R("更新成功");
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public  R getOne(@PathVariable int id){
        try{
            Slider slider = sliderService.find(id);
            return new R(slider);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }
}
