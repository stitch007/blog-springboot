package run.stitch.blog.service.impl;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.service.TencentCosService;

import java.io.File;
import java.io.IOException;

@Service
public class TencentCosServiceImpl implements TencentCosService {
    @Value("${spring.cos.base-url}")
    private String baseUrl;
    @Value("${spring.cos.bucket}")
    private String bucket;
    @Value("${spring.cos.folder}")
    private String folder;

    @Autowired
    COSClient cosClient;

    @Override
    public String upload(MultipartFile file, String fileName, String suffix) {
        String fullName = this.folder + '/' + fileName;
        try {
            File localFile = File.createTempFile(String.valueOf(System.currentTimeMillis()), suffix);
            file.transferTo(localFile);
            PutObjectRequest objectRequest = new PutObjectRequest(bucket, fullName, localFile);
            cosClient.putObject(objectRequest);
        } catch (IOException e) {
            throw new BizException(500, "上传文件失败");
        }
        return baseUrl + '/' + fullName;
    }
}
