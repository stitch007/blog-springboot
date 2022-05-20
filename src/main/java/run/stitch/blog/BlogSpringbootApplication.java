package run.stitch.blog;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("run.stitch.blog.repository")
public class BlogSpringbootApplication {
    public static void main(String[] args) {
        SpringApplication.run(BlogSpringbootApplication.class, args);
    }
}
