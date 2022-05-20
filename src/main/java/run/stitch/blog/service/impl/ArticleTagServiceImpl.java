package run.stitch.blog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import run.stitch.blog.dto.ArticleTagDTO;
import run.stitch.blog.entity.ArticleTag;
import run.stitch.blog.exception.BizException;
import run.stitch.blog.repository.ArticleTagRepository;
import run.stitch.blog.service.ArticleTagService;

import java.util.List;

@Service
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagRepository, ArticleTag> implements ArticleTagService {
    public void bind(ArticleTagDTO articleTagDTO) {
        deleteByArticleId(articleTagDTO.getArticleId());
        List<ArticleTag> articleTags = articleTagDTO.getTagIds().stream().map(tagId -> ArticleTag
                .builder().articleId(articleTagDTO.getArticleId()).tagId(tagId).build()).toList();
        if (!saveBatch(articleTags)) {
            throw new BizException(500, "绑定标签失败");
        }
    }

    @Override
    public void deleteByArticleId(Integer articleId) {
        remove(new LambdaQueryWrapper<ArticleTag>().eq(ArticleTag::getArticleId, articleId));
    }
}
