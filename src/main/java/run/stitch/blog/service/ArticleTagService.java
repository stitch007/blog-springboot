package run.stitch.blog.service;

import run.stitch.blog.dto.ArticleTagDTO;

public interface ArticleTagService {
    boolean bind(ArticleTagDTO articleTagDTO);

    boolean deleteByArticleId(Integer articleId);
}
