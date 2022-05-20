package run.stitch.blog.service;

import run.stitch.blog.dto.ArticleTagDTO;

public interface ArticleTagService {
    void bind(ArticleTagDTO articleTagDTO);

    void deleteByArticleId(Integer articleId);
}
