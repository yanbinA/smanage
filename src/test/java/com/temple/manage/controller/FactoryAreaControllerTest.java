package com.temple.manage.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.temple.manage.common.validators.group.Modify;
import com.temple.manage.domain.dto.FactoryAreaDto;
import com.temple.manage.util.AbstractRestControllerTest;
import com.temple.manage.util.LogInUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.junit.Test;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotNull;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class FactoryAreaControllerTest extends AbstractRestControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Test
    public void test() {



    }

    @Test
    public void list() throws Exception {
        String token = LogInUtils.getTokenForLogin("admin", this.getMockMvc());
        String areaList = this.getMockMvc().perform(get("/api/area/list")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn()
                .getResponse()
                .getContentAsString();
        System.out.println(areaList);
    }

    @Test
    public void insert() throws Exception {
        String token = LogInUtils.getTokenForLogin("admin", this.getMockMvc());
        for (int i = 0; i < 5; i++) {
            FactoryAreaDto areaDto = new FactoryAreaDto();
            areaDto.setName(String.format("车间%03d", i));
            areaDto.setSerialNumber(String.format("SN%03d", i));
            this.getMockMvc().perform(post("/api/area")
                    .header("Authorization", "Bearer " + token)
                    .content(OBJECT_MAPPER.writeValueAsString(areaDto))
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk());
        }

    }

    @PostMapping("/modify")
    @Operation(summary = "修改车间信息", security = @SecurityRequirement(name = "Authorization"))
    public void update(@Validated(value = Modify.class) @RequestBody FactoryAreaDto factoryAreaDto) {
    }


    @PostMapping("/remove")
    @Operation(summary = "删除车间", security = @SecurityRequirement(name = "Authorization"))
    public void delete(@NotNull @RequestParam("id") Integer id) {
    }
}
