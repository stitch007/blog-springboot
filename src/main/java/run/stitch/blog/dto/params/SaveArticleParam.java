package run.stitch.blog.dto.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SaveArticleParam {
    @NotNull(message = "分类不能为空")
    private UpdateCategoryParam category;

    @NotNull(message = "标签不能为空")
    private List<UpdateTagParam> tags;

    @NotBlank(message = "文章标题不能为空")
    @Length(max = 128, message = "文章标题不能超过128个字符")
    private String title;

    @NotBlank(message = "文章内容不能为空")
    private String content;
}
