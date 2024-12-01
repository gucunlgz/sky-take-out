package com.sky.controller.admin;

import com.aliyuncs.exceptions.ClientException;
import com.sky.constant.MessageConstant;
import com.sky.result.Result;
import com.sky.utils.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/admin/common")
@Slf4j
public class CommonController {
    @Autowired
    private AliOssUtil aliOssUtil;

    @PostMapping("upload")
    public Result load(@RequestParam MultipartFile file)  {
        log.info("上传文件{}",file.getOriginalFilename());
        String upload = null;
        try {
            upload = aliOssUtil.upload(file);
            return Result.success(upload);
        } catch (Exception e) {
            log.error(e.getMessage(),e);
        }
       return Result.error(MessageConstant.UPLOAD_FAILED);
    }
}
