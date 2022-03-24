package com.temple.manage.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.alibaba.excel.write.metadata.fill.FillWrapper;
import com.temple.manage.common.api.R;
import com.temple.manage.common.api.ResultCode;
import com.temple.manage.common.exception.Asserts;
import com.temple.manage.common.utils.FileUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/api/file")
@Tag(name = "图片上传")
@Slf4j
public class FileController {
    private final String baseUrl;
    private final String basePath;

    public FileController(
            @Value("${upload.location}") String location,
            @Value("${upload.remote}") String remote) {
        this.baseUrl = remote;
        this.basePath = location;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "图片上传", security = @SecurityRequirement(name = "Authorization"))
    public R<String> upload(@RequestPart("file") MultipartFile file) {
        if (StringUtils.isEmpty(file.getOriginalFilename())) {
            Asserts.fail(ResultCode.FILE_NOT_EXIST);
        }
        String filePath = FileUtil.generalFilename(file.getOriginalFilename());
        try (FileOutputStream inputStream = new FileOutputStream(filePath)){
            inputStream.write(file.getBytes());
        } catch (IOException e) {
            Asserts.fail(e.getMessage());
        }
        return R.success(FileUtil.getUrl(filePath));
    }
}
