package run.stitch.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import run.stitch.blog.dto.ArticleDTO;
import run.stitch.blog.dto.ArticleTagDTO;
import run.stitch.blog.dto.params.SaveArticleParam;
import run.stitch.blog.dto.params.UpdateArticleParam;
import run.stitch.blog.dto.params.UpdateCategoryParam;
import run.stitch.blog.dto.params.UpdateTagParam;
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

import static run.stitch.blog.util.StatusCode.*;

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
        if (ObjectUtils.isEmpty(articleDTO)) {
            throw new BizException(NOT_FOUND);
        }
        return articleDTO;
    }

    @Override
    @Transactional
    public Integer saveArticle(SaveArticleParam saveArticleParam) {
        Integer categoryId = this.saveOrUpdateCategory(saveArticleParam.getCategory());
        if (ObjectUtils.isEmpty(categoryId)) {
            return null;
        }
        Article article = Copy.copyObject(saveArticleParam, Article.class);
        article.setCategoryId(categoryId);
        article.setUserId(Integer.parseInt(StpUtil.getLoginId().toString()));
        if (articleRepository.insert(article) <= 0) {
            return null;
        }
        if (!this.saveAndBindTags(article.getId(), saveArticleParam.getTags())) {
            return null;
        }
        return article.getId();
    }

    @Override
    @Transactional
    public Integer updateArticle(UpdateArticleParam updateArticleParam) {
        Integer categoryId = this.saveOrUpdateCategory(updateArticleParam.getCategory());
        if (ObjectUtils.isEmpty(categoryId)) {
            return null;
        }
        Article article = Copy.copyObject(updateArticleParam, Article.class);
        article.setCategoryId(categoryId);
        if (articleRepository.updateById(article) < 0) {
            return null;
        }
        if (!this.saveAndBindTags(article.getId(), updateArticleParam.getTags())) {
            return null;
        }
        return article.getId();
    }

    @Override
    public boolean deleteArticles(String[] ids) {
        return articleRepository.deleteBatchIds(Arrays.asList(ids)) >= 0;
    }

    private Integer saveOrUpdateCategory(UpdateCategoryParam updateCategoryRequest) {
        Integer categoryId = null;
        if (ObjectUtils.isEmpty(updateCategoryRequest.getId())) {
            categoryId = categoryService.saveCategory(updateCategoryRequest);
        } else {
            categoryId = categoryService.updateCategory(updateCategoryRequest);
        }
        return categoryId;
    }

    private boolean saveAndBindTags(Integer articleId, List<UpdateTagParam> updateTagRequests) {
        List<Integer> tagIds = tagService.saveTags(updateTagRequests);
        if (ObjectUtils.isEmpty(tagIds)) {
            return false;
        }
        return articleTagService.bind(ArticleTagDTO.builder().articleId(articleId).tagIds(tagIds).build());
    }
}
