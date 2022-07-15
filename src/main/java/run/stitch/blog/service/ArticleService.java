package run.stitch.blog.service;

import run.stitch.blog.dto.ArticleDTO;
import run.stitch.blog.dto.params.SaveArticleParam;
import run.stitch.blog.dto.params.UpdateArticleParam;

import java.util.List;

public interface ArticleService {
    List<ArticleDTO> getArticles();

    List<ArticleDTO> getArticlesByIds(String[] ids);

    ArticleDTO getArticleByTitle(String title);

    Integer saveArticle(SaveArticleParam saveArticleParam);

    Integer updateArticle(UpdateArticleParam updateArticleParam);

    boolean deleteArticles(String[] ids);
}
