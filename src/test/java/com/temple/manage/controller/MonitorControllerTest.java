package com.temple.manage.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.temple.manage.domain.dto.MonitoringItemDto;
import com.temple.manage.domain.dto.MonitoringPointDto;
import com.temple.manage.util.AbstractRestControllerTest;
import com.temple.manage.util.LogInUtils;
import org.junit.Test;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MonitorControllerTest extends AbstractRestControllerTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    @Test
    public void insert() throws Exception {
        String[] auditors = {"张主", "李主", "王主"};
        String token = LogInUtils.getTokenForLogin("admin", this.getMockMvc());
        for (int i = 0; i < 5; i++) {
            MonitoringPointDto areaDto = new MonitoringPointDto();
            areaDto.setFactoryAreaId(1 + i);
            for (int j = 0; j < i + 2; j++) {
                areaDto.setAreaName(String.format("%03d车间-区域%02d", i, (j % 3)));
                areaDto.setSerialNumber(j + 1);
                areaDto.setAuditor(auditors[j%3]);
                areaDto.setAbscissa(0);
                areaDto.setOrdinate(0);
                List<MonitoringItemDto> list = new ArrayList<>();
                for (int k = 0; k < j + 3; k++) {
                    MonitoringItemDto itemDto = new MonitoringItemDto();
                    itemDto.setItemNumber(String.format("%03d车-区域%02d-%02d", i, (j % 3), k));
                    itemDto.setRemark("remark");
                    itemDto.setItemUrl("image");
                    list.add(itemDto);
                }
                areaDto.setItemList(list);
                this.getMockMvc().perform(post("/api/monitor/save")
                        .header("Authorization", "Bearer " + token)
                        .content(OBJECT_MAPPER.writeValueAsString(areaDto))
                        .contentType(MediaType.APPLICATION_JSON))
                        .andExpect(status().isOk());
            }

        }
    }
}
