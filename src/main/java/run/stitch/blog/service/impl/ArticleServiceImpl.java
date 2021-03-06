package run.stitch.blog.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import run.stitch.blog.dto.ArticleDTO;
import run.stitch.blog.dto.ArticleTagDTO;
import run.stitch.blog.dto.TagDTO;
import run.stitch.blog.dto.params.SaveArticleParam;
import run.stitch.blog.dto.params.UpdateArticleParam;
import run.stitch.blog.entity.Article;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.ArticleRepository;
import run.stitch.blog.service.ArticleService;
import run.stitch.blog.service.ArticleTagService;
import run.stitch.blog.service.CategoryService;
import run.stitch.blog.service.TagService;
import run.stitch.blog.utils.CopyUtil;

import java.util.Arrays;
import java.util.List;

import static run.stitch.blog.enums.StatusCodeEnum.*;

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
        Integer categoryId = categoryService.saveOrUpdateCategory(saveArticleParam.getCategory());
        if (ObjectUtils.isEmpty(categoryId)) {
            return null;
        }
        Article article = CopyUtil.copyObject(saveArticleParam, Article.class);
        article.setCategoryId(categoryId);
        article.setUserId(Integer.parseInt(StpUtil.getLoginId().toString()));
        if (articleRepository.insert(article) <= 0) {
            return null;
        }
        if (this.saveAndBindTags(article.getId(), saveArticleParam.getTags())) {
            return null;
        }
        return article.getId();
    }

    @Override
    @Transactional
    public Integer updateArticle(UpdateArticleParam updateArticleParam) {
        Integer categoryId = categoryService.saveOrUpdateCategory(updateArticleParam.getCategory());
        if (ObjectUtils.isEmpty(categoryId)) {
            return null;
        }
        Article article = CopyUtil.copyObject(updateArticleParam, Article.class);
        article.setCategoryId(categoryId);
        if (articleRepository.updateById(article) < 0) {
            return null;
        }
        if (this.saveAndBindTags(article.getId(), updateArticleParam.getTags())) {
            return null;
        }
        return article.getId();
    }

    @Override
    public boolean deleteArticles(String[] ids) {
        return articleRepository.deleteBatchIds(Arrays.asList(ids)) >= 0;
    }

    private boolean saveAndBindTags(Integer articleId, List<TagDTO> tagDTOs) {
        List<Integer> tagIds = tagService.saveTags(tagDTOs);
        if (ObjectUtils.isEmpty(tagIds)) {
            return true;
        }
        return !articleTagService.bind(ArticleTagDTO.builder().articleId(articleId).tagIds(tagIds).build());
    }
}
