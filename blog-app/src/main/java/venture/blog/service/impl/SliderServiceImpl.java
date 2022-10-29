package venture.blog.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import venture.blog.mapper.SliderMapper;
import venture.blog.pojo.Slider;
import venture.blog.service.SliderService;
import venture.blog.utils.RedisConstants;

import java.util.Date;
import java.util.List;

@Service
public class SliderServiceImpl implements SliderService {
    @Autowired
    SliderMapper sliderMapper;

    @Autowired
    StringRedisTemplate template;

    @Override
    public PageInfo<Slider> findByPageAndKey(int size, int current, String key) {
        PageHelper.startPage(current, size);
        List<Slider> list = sliderMapper.findAll(key);
        PageInfo<Slider> pi = new PageInfo<>(list);
        return pi;
    }

    @Override
    public List<Slider> findShowing() {
        ValueOperations<String, String> op = template.opsForValue();
        String configStr = op.get(RedisConstants.SLIDERS_KEY);
        if(configStr != null){
            return JSONUtil.toList(configStr,Slider.class);
        }
        List<Slider> list = sliderMapper.findShowing();
        op.set(RedisConstants.SLIDERS_KEY,JSONUtil.toJsonStr(list));
        return list;
    }

    @Override
    public boolean insert(Slider slider) {
        String date = DateUtil.format(new Date(), "yyyy-MM-dd");
        slider.setPostdate(date);
        return sliderMapper.insert(slider);
    }

    @Override
    public void del(int[] ids) {
        for (int id : ids) {
            sliderMapper.del(id);
        }
    }

    @Override
    public boolean update(Slider slider) {
        return sliderMapper.update(slider);
    }

    @Override
    public Slider find(int id) {
        return sliderMapper.find(id);
    }
}
