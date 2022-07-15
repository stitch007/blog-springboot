package run.stitch.blog.dto.params;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginParam {
    @NotBlank(message = "用户名不能为空")
    @Pattern(regexp = "^[A-Za-z0-9_-]{6,18}$", message = "用户名应为6-18位字母,数字或_-的组合")
    private String username;

    @NotBlank(message = "密码不能为空")
    @Length(min = 8, max = 18, message = "密码长度应为8-18位")
    @Pattern(regexp = "^[A-Za-z\\d$@$!%*#?&]+$", message = "密码不能有\'$@$!%*#?&\'以外的特殊字符")
    private String password;

    @NotBlank(message = "验证码不能为空")
    @Pattern(regexp = "^[A-Za-z0-9]{4}$", message = "验证码应为4位字母或数字的组合")
    private String code;
}
