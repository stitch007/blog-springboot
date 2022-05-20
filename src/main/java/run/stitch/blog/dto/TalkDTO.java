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
public class TalkDTO {
    private Integer id;

    private String content;

    private String device;

    private Date createTime;
}
