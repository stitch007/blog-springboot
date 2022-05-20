package run.stitch.blog.config;

import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.qcloud.cos.region.Region;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TencentCosConfig {
    @Value("${spring.cos.secret-id}")
    private String secretId;
    @Value("${spring.cos.secret-key}")
    private String secretKey;
    @Value("${spring.cos.region}")
    private String region;

    @Bean
    public COSClient cosClient() {
        COSCredentials cosCredentials = new BasicCOSCredentials(secretId, secretKey);
        Region region = new Region(this.region);
        ClientConfig clientConfig = new ClientConfig(region);
        return new COSClient(cosCredentials, clientConfig);
    }
}
