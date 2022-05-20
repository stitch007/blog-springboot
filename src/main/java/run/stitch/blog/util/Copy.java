package run.stitch.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import run.stitch.blog.exception.BizException;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Copy {
    public static <T> T copyObject(Object source, Class<T> clazz) {
        T result = null;
        try {
            result = clazz.getConstructor().newInstance();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BizException(500, "对象创建失败");
        }
        BeanUtils.copyProperties(source, result);
        return result;
    }

    public static <T, U> List<T> copyList(List<U> source, Class<T> clazz) {
        List<T> result = new ArrayList<>();
        source.forEach(item -> result.add(copyObject(item, clazz)));
        return result;
    }
}
