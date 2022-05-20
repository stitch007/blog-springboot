package run.stitch.blog.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import run.stitch.blog.entity.Tag;

import java.util.List;

@Repository
public interface TagRepository extends BaseMapper<Tag> {
    int insertBatch(@Param("tags") List<Tag> tags);
}
