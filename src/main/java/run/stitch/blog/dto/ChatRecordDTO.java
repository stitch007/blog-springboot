package run.stitch.blog.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRecordDTO {
    private Integer id;

    private Integer userId;

    private String username;

    private String avatarUrl;

    private String content;

    private Date createTime;
}
