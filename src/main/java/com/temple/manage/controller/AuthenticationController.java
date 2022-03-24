package com.temple.manage.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.temple.manage.common.api.R;
import com.temple.manage.domain.dto.LoginDto;
import com.temple.manage.security.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * Controller to authenticate users
 * </p>
 *
 * @author messi
 * @package com.temple.manage.controller
 * @description Controller to authenticate users
 * @date 2021-12-25 17:02
 * @verison V1.0.0
 */
@RestController
@RequestMapping("/api")
@Tag(name = "用户登录")
public class AuthenticationController {

    private final TokenProvider tokenProvider;

    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    public AuthenticationController(TokenProvider tokenProvider, AuthenticationManagerBuilder authenticationManagerBuilder) {
        this.tokenProvider = tokenProvider;
        this.authenticationManagerBuilder = authenticationManagerBuilder;
    }

    @PostMapping("/authenticate")
    @Operation(summary = "5S用户登录")
    public R<JWTToken> authorize(@Valid @RequestBody LoginDto loginDto) {

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        boolean rememberMe = loginDto.getRememberMe() != null && loginDto.getRememberMe();
        String jwt = tokenProvider.createToken(authentication, rememberMe);
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst().orElse("ROLE_AUDIT");
        return R.success(new JWTToken(jwt, authorities));
    }

    @Data
    @Schema(description = "Token信息")
    static class JWTToken {
        @JsonProperty("token")
        @Schema(description = "token")
        private String idToken;
        @Schema(description = "角色,'ROLE_ADMIN'-管理员,'ROLE_AUDIT'-审核员")
        private String role;

        JWTToken(String idToken, String role) {
            this.idToken = idToken;
            this.role = role;
        }
    }
}
