package run.stitch.blog.service;

import org.springframework.web.multipart.MultipartFile;

public interface TencentCosService {
    String upload(MultipartFile file, String fileName, String suffix);
}
