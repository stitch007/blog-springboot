package run.stitch.blog.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import run.stitch.blog.dto.ArticleDTO;
import run.stitch.blog.entity.Article;

import java.util.List;

@Repository
public interface ArticleRepository extends BaseMapper<Article> {
    List<ArticleDTO> getArticles();

    List<ArticleDTO> getArticlesByIds(@Param("ids") String[] ids);

    ArticleDTO getArticleByTitle(@Param("title") String title);
}
