package com.sky.controller.admin;

import com.sky.annotation.AutoFill;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {

    @Autowired
    private AliOssUtil aliOssUtil;
@PostMapping("/upload")
    public Result<String> upload(MultipartFile file) throws IOException {
    log.info("文件上传：{}",file);


    try {
        // 获取原始文件名
        String originalFilename = file.getOriginalFilename();

        // 截取文件后缀名
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));

        // 生成新文件名
        String objectName = UUID.randomUUID().toString() + extension;

        // 上传文件到阿里云
        String filePath = aliOssUtil.upload(file.getBytes(), objectName);

        // 返回文件访问路径
        return Result.success(filePath);

    }
    catch (IOException e) {
        log.error("文件上传失败", e);
        throw new RuntimeException("文件上传失败");
    }

}

}
