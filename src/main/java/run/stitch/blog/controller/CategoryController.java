package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.CategoryDTO;
import run.stitch.blog.service.CategoryService;
import run.stitch.blog.util.Result;

import java.util.HashMap;
import java.util.List;

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
    public Result<?> saveCategory(@RequestBody CategoryDTO categoryDTO) {
        if (categoryDTO.getId() != null) {
            return Result.error(403, "新增操作不能携带id");
        }
        return Result.ok(new HashMap<>() {{
            put("id", categoryService.saveOrUpdateCategory(categoryDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/categories")
    public Result<?> updateCategory(@RequestBody CategoryDTO categoryDTO) {
        if (categoryDTO.getId() == null) {
            return Result.error(403, "修改操作必须携带id");
        }
        return Result.ok(new HashMap<>() {{
            put("id", categoryService.saveOrUpdateCategory(categoryDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/categories/{ids}")
    public Result<?> deleteCategories(@PathVariable("ids") String[] ids) {
        if (categoryService.deleteCategories(ids)) {
            return Result.ok();
        }
        return Result.error(500, "删除失败");
    }
}
