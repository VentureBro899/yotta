package venture.blog.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import venture.blog.pojo.Navigator;
import venture.blog.service.NavigatorService;
import venture.blog.vo.R;

import java.util.List;

@RestController
@RequestMapping("/admin/navigator")
public class NavigatorController {
    @Autowired
    NavigatorService navigatorService;

    @GetMapping("/list")
    public R getList(){
        try {
            List<Navigator> list = navigatorService.findAll();
            return new R(list);
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PostMapping()
    public R addOne(@RequestBody Navigator navigator){
        try {
            navigatorService.insert(navigator);
            // 返回插入后的主键id
            return new R(navigator.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @DeleteMapping()
    public R delSome(@RequestBody int[] ids){
        try {
            navigatorService.del(ids);
            return new R();
        } catch (Exception e) {
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @PutMapping()
    public R updOne(@RequestBody Navigator navigator){
        try{
            navigatorService.update(navigator);
            return new R();
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public  R getOne(@PathVariable int id){
        try{
            Navigator navigator = navigatorService.find(id);
            return new R(navigator);
        }catch(Exception e){
            e.printStackTrace();
            return new R(500,e.getMessage());
        }
    }
}
