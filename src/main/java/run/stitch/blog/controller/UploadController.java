package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import run.stitch.blog.service.TencentCosService;
import run.stitch.blog.util.Result;

import java.util.HashMap;

@RestController
public class UploadController {
    @Autowired
    TencentCosService tencentCosService;

    @SaCheckRole("ADMIN")
    @PostMapping("/upload/image")
    public Result<?> uploadImageToCos(@RequestParam(value = "file") MultipartFile file) {
        if (ObjectUtils.isEmpty(file)) {
            return Result.error(401, "文件不能为空");
        }
        String originalFileName = file.getOriginalFilename();
        if (ObjectUtils.isEmpty(originalFileName)) {
            return Result.error(401, "获取文件信息失败");
        }
        int index = originalFileName.lastIndexOf(".");
        String suffix = originalFileName.substring(index);
        if (!suffix.equals(".jpg") && !suffix.equals(".png") && !suffix.equals(".jpeg")) {
            return Result.error(401, "仅支持jpg、png、jpeg格式");
        }
        String fileName = originalFileName.substring(0, index) + "-" + System.currentTimeMillis() + suffix;
        return Result.ok(new HashMap<>() {{
            put("url", tencentCosService.upload(file, fileName, suffix));
        }});
    }
}
