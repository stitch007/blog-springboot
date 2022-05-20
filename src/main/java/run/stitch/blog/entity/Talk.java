package run.stitch.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName("talk")
public class Talk {
    @TableId(type = IdType.AUTO)
    private Integer id;

    private Integer userId;

    private String content;

    private String device;

    // 1-已删除，0-未删除
    private Boolean deleted;

    private Date createTime;

    private Date updateTime;
}
