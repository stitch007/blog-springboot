package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.CategoryDTO;
import run.stitch.blog.service.CategoryService;
import run.stitch.blog.util.Result;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

import static run.stitch.blog.util.StatusCode.FAIL;

@RestController
public class CategoryController {
    @Autowired
    CategoryService categoryService;

    @GetMapping("/categories")
    public Result<List<CategoryDTO>> getCategories() {
        return Result.ok(categoryService.getCategories());
    }

    @GetMapping("/categories/{ids}")
    public Result<List<CategoryDTO>> getCategoriesByIds(@PathVariable("ids") String[] ids) {
        return Result.ok(categoryService.getCategoriesByIds(ids));
    }

    @SaCheckRole("ADMIN")
    @PostMapping("/categories")
    public Result saveCategory(@RequestBody @NotNull CategoryDTO categoryDTO) {
        Integer id = categoryService.saveOrUpdateCategory(categoryDTO);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/categories")
    public Result updateCategory(@RequestBody @NotNull CategoryDTO categoryDTO) {
        Integer id = categoryService.saveOrUpdateCategory(categoryDTO);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/categories/{ids}")
    public Result deleteCategories(@PathVariable("ids") String[] ids) {
        if (categoryService.deleteCategories(ids)) {
            return Result.ok();
        }
        return Result.error(FAIL);
    }
}
