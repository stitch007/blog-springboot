package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.ArticleDTO;
import run.stitch.blog.service.ArticleService;
import run.stitch.blog.util.Result;

import java.util.HashMap;
import java.util.List;

@RestController
public class ArticleController {
    @Autowired
    ArticleService articleService;

    @GetMapping("/articles")
    public Result<?> getArticles(@RequestParam(name = "title", required = false, defaultValue = "") String title) {
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
    public Result<?> saveArticle(@RequestBody ArticleDTO articleDTO) {
        if (articleDTO.getId() != null) {
            return Result.error(401, "新增操作不能携带id");
        }
        return Result.ok(new HashMap<>(){{
            put("id", articleService.saveArticle(articleDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/articles")
    public Result<?> updateArticle(@RequestBody ArticleDTO articleDTO) {
        if (articleDTO.getId() == null) {
            return Result.error(401, "修改操作必须携带id");
        }
        return Result.ok(new HashMap<>(){{
            put("id", articleService.updateArticle(articleDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/articles/{ids}")
    public Result<?> deleteArticles(@PathVariable("ids") String[] ids) {
        if (articleService.deleteArticles(ids)) {
            return Result.ok();
        }
        return Result.error(500, "删除失败");
    }
}
