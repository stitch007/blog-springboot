package run.stitch.blog.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import run.stitch.blog.entity.User;

@Repository
public interface UserRepository extends BaseMapper<User> {

}
