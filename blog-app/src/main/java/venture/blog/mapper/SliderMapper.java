package venture.blog.mapper;

import org.apache.ibatis.annotations.Param;
import venture.blog.pojo.Slider;

import java.util.List;

public interface SliderMapper {
    public List<Slider> findAll(@Param("key") String key);

    public List<Slider> findShowing();

    public boolean insert(Slider slider);

    public boolean del(int id);

    public boolean update(Slider slider);

    public Slider find(int id);
}
