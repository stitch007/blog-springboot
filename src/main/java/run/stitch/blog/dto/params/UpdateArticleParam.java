package run.stitch.blog.dto.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.validation.constraints.NotNull;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateArticleParam extends SaveArticleParam {
    @NotNull(message = "id不能为空")
    private Integer id;
}
