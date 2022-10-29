package venture.blog.service;

import com.github.pagehelper.PageInfo;
import venture.blog.pojo.Slider;

import java.util.List;

public interface SliderService {
    public PageInfo<Slider> findByPageAndKey(int size, int current, String key);

    public List<Slider> findShowing();

    public boolean insert(Slider slider);

    public void del(int[] ids);

    public boolean update(Slider slider);

    public Slider find(int id);
}
