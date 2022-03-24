package com.temple.manage.PoiUtils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import lombok.Data;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExportTest {
    public static void main(String[] args) {
        String fillTemplate = "/home/willso/workspace/smanage/src/main/resources/excel/s5.xls";
        String fileName = "./listFill" + System.currentTimeMillis() + ".xlsx";
        // 这里 会填充到第一个sheet， 然后文件流会自动关闭
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(fillTemplate).build();
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        WriteSheet writeSheet1 = EasyExcel.writerSheet().build();
        excelWriter.fill(new FillWrapper("score", manageScores()), fillConfig, writeSheet1);
        excelWriter.fill(new FillWrapper("image", manageImages()), fillConfig, writeSheet1);
        excelWriter.fill(new FillWrapper("audit", auditImages()), fillConfig, writeSheet1);
        Map<String, String> map = new HashMap<>();
        map.put("dateSpan", "2002到2030");
        excelWriter.fill(map, writeSheet1);
        // 千万别忘记关闭流
        excelWriter.finish();
    }

    private static List<ManageScore> manageScores() {
        List<ManageScore> scoreList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            ManageScore score = new ManageScore();
            score.setId(i);
            score.setSort(i);
            score.setScore(BigDecimal.valueOf(i).divide(BigDecimal.valueOf(3), 2, RoundingMode.DOWN));
            score.setName("谁信用户" + i);
            scoreList.add(score);
        }
        return scoreList;
    }

    private static List<ManageImage> manageImages() {
        List<ManageImage> scoreList = new ArrayList<>();
//        for (int i = 0; i < 20; i++) {
//            ManageImage score = new ManageImage();
//            score.setId(i);
//
//            score.setDate(LocalDate.now().plusDays(i).format(DateTimeFormatter.ISO_LOCAL_DATE));
//            score.setAreaName("A00" + i);
//            score.setUrl("https://www.baidu.com");
//            score.setName("谁信Image" + i);
//            scoreList.add(score);
//        }
        return scoreList;
    }

    private static List<AuditImage> auditImages() {
        List<AuditImage> scoreList = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            AuditImage score = new AuditImage();
            score.setId(i);
            score.setSort(i);
            score.setTotal(i);
            score.setName("谁信Audit" + i);
            scoreList.add(score);
        }
        return scoreList;
    }

    @Data
    static class ManageScore{
        private Integer id;
        private Integer sort;
        private BigDecimal score;
        private String name;
    }

    @Data
    static class ManageImage{
        private Integer id;
        private String date;
        private String areaName;
        private String name;
        private String url;
    }

    @Data
    static class AuditImage{
        private Integer id;
        private Integer sort;
        private Integer total;
        private String name;
    }
}
