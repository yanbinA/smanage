package com.temple.manage.domain.dto;

import com.temple.manage.entity.enums.UserGenderEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 用户注册类
 * </p>
 *
 * @author messi
 * @package com.temple.manage.domain
 * @description 用户注册类
 * @date 2021-12-20 22:12
 * @verison V1.0.0
 */
@Data
@Schema(name = "用户注册类")
public class UserRegisterDto {
    @Schema(description = "昵称")
    @NotBlank
    private String nickname;
    @Schema(description = "性别")
    private UserGenderEnum gender;

    private String avatar;
    @Schema(description = "用户名")
    @NotBlank
    private String username;
    @Schema(description = "密码")
    private String password;
}
