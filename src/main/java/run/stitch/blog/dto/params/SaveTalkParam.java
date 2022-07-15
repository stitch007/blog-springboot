package run.stitch.blog.dto.params;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;

@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class SaveTalkParam {
    @NotBlank(message = "内容不能为空")
    private String content;

    @Length(max = 32, message = "最长为32个字符")
    private String device;
}
