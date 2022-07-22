package run.stitch.blog.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.springframework.stereotype.Repository;
import run.stitch.blog.entity.ChatRecord;

@Repository
public interface ChatRecordRepository extends BaseMapper<ChatRecord> {

}
