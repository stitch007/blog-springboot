package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.ArticleDTO;
import run.stitch.blog.dto.params.SaveArticleParam;
import run.stitch.blog.dto.params.UpdateArticleParam;
import run.stitch.blog.service.ArticleService;
import run.stitch.blog.util.Result;

import java.util.HashMap;
import java.util.List;

import static run.stitch.blog.util.StatusCode.FAIL;

@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @GetMapping("/articles")
    public Result getArticles(@RequestParam(name = "title", required = false, defaultValue = "") String title) {
        if (title.isEmpty()) {
            return Result.ok(articleService.getArticles());
        }
        return Result.ok(articleService.getArticleByTitle(title));
    }

    @GetMapping("/articles/{ids}")
    public Result<List<ArticleDTO>> getArticlesByIds(@PathVariable("ids") String[] ids) {
        return Result.ok(articleService.getArticlesByIds(ids));
    }

    @SaCheckRole("ADMIN")
    @PostMapping("/articles")
    public Result saveArticle(@RequestBody @Validated SaveArticleParam saveArticleParam) {
        Integer id = articleService.saveArticle(saveArticleParam);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/articles")
    public Result updateArticle(@RequestBody @Validated UpdateArticleParam updateArticleParam) {
        Integer id = articleService.updateArticle(updateArticleParam);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/articles/{ids}")
    public Result deleteArticles(@PathVariable("ids") String[] ids) {
        if (articleService.deleteArticles(ids)) {
            return Result.ok();
        }
        return Result.error(FAIL);
    }
}
