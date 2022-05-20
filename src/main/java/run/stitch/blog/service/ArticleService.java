package run.stitch.blog.service;

import run.stitch.blog.dto.ArticleDTO;

import java.util.List;

public interface ArticleService {
    List<ArticleDTO> getArticles();

    List<ArticleDTO> getArticlesByIds(String[] ids);

    ArticleDTO getArticleByTitle(String title);

    Integer saveArticle(ArticleDTO articleDTO);

    Integer updateArticle(ArticleDTO articleDTO);

    boolean deleteArticles(String[] ids);
}
