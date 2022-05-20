package run.stitch.blog.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import run.stitch.blog.entity.Category;

@Repository
public interface CategoryRepository extends BaseMapper<Category> {

}
