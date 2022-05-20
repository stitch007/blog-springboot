package run.stitch.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import run.stitch.blog.dto.ArticleDTO;
import run.stitch.blog.dto.ArticleTagDTO;
import run.stitch.blog.entity.Article;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.ArticleRepository;
import run.stitch.blog.service.ArticleService;
import run.stitch.blog.service.ArticleTagService;
import run.stitch.blog.service.CategoryService;
import run.stitch.blog.service.TagService;
import run.stitch.blog.util.Copy;

import java.util.Arrays;
import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {
    @Autowired
    ArticleRepository articleRepository;

    @Autowired
    CategoryService categoryService;

    @Autowired
    TagService tagService;

    @Autowired
    ArticleTagService articleTagService;

    @Override
    public List<ArticleDTO> getArticles() {
        return articleRepository.getArticles();
    }

    @Override
    public List<ArticleDTO> getArticlesByIds(String[] ids) {
        return articleRepository.getArticlesByIds(ids);
    }

    @Override
    public ArticleDTO getArticleByTitle(String title) {
        ArticleDTO articleDTO = articleRepository.getArticleByTitle(title);
        if (articleDTO != null) {
            return articleDTO;
        }
        throw new BizException(404, "文章不存在");
    }

    @Override
    @Transactional
    public Integer saveArticle(ArticleDTO articleDTO) {
        Integer categoryId = categoryService.saveOrUpdateCategory(articleDTO.getCategory());
        Article article = Copy.copyObject(articleDTO, Article.class);
        article.setCategoryId(categoryId);
        article.setUserId(Integer.parseInt(StpUtil.getLoginId().toString()));
        articleRepository.insert(article);
        List<Integer> tagIds = tagService.saveTags(articleDTO.getTags());
        articleTagService.bind(ArticleTagDTO.builder().articleId(article.getId()).tagIds(tagIds).build());
        return article.getId();
    }

    @Override
    @Transactional
    public Integer updateArticle(ArticleDTO articleDTO) {
        Integer categoryId = null;
        if (articleDTO.getCategory() != null) {
            categoryId = categoryService.saveOrUpdateCategory(articleDTO.getCategory());
        }
        Article article = Copy.copyObject(articleDTO, Article.class);
        if (categoryId != null) {
            article.setCategoryId(categoryId);
        }
        articleRepository.updateById(article);
        if (articleDTO.getTags() != null) {
            List<Integer> tagIds = tagService.saveTags(articleDTO.getTags());
            articleTagService.bind(ArticleTagDTO.builder().articleId(article.getId()).tagIds(tagIds).build());
        }
        return article.getId();
    }

    @Override
    public boolean deleteArticles(String[] ids) {
        return articleRepository.deleteBatchIds(Arrays.asList(ids)) > 0;
    }
}
