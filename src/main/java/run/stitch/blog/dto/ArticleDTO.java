package run.stitch.blog.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ArticleDTO {
    private Integer id;

    private CategoryDTO category;

    private List<TagDTO> tags;

    private String title;

    private String content;

    private String summary;

    private String coverImage;

    private Date createTime;

    private Date updateTime;
}
