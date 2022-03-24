package com.temple.manage.domain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * 登录信息
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain.dto
 * @description 登录信息
 * @date 2021-12-25 17:16
 * @verison V1.0.0
 */
@Data
@Schema(description = "登录信息")
public class LoginDto {
    @NotNull
    @Size(min = 1, max = 50)
    @Schema(description = "用户名称,admin管理员,其他为审核员", defaultValue = "admin")
    private String username;

    @Schema(description = "密码", hidden = true)
    private String password = "123456";
    @Schema( hidden = true)
    private Boolean rememberMe;
}
