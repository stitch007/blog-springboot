package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.TagDTO;
import run.stitch.blog.service.TagService;
import run.stitch.blog.util.Result;

import java.util.HashMap;
import java.util.List;

@RestController
public class TagController {
    @Autowired
    TagService tagService;

    @GetMapping("/tags")
    public Result<List<TagDTO>> getTags() {
        return Result.ok(tagService.getTags());
    }

    @GetMapping("/tags/{ids}")
    public Result<List<TagDTO>> getTagsByIds(@PathVariable("ids") String[] ids) {
        return Result.ok(tagService.getTagsByIds(ids));
    }

    @SaCheckRole("ADMIN")
    @PostMapping("/tags")
    public Result<?> saveTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO.getId() != null) {
            return Result.error(400, "新增操作不能携带id");
        }
        return Result.ok(new HashMap<>() {{
            put("id", tagService.saveOrUpdateTag(tagDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/tags")
    public Result<?> updateTag(@RequestBody TagDTO tagDTO) {
        if (tagDTO.getId() == null) {
            return Result.error(400, "修改操作必须携带id");
        }
        return Result.ok(new HashMap<>() {{
            put("id", tagService.saveOrUpdateTag(tagDTO));
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/tags/{ids}")
    public Result<?> deleteTags(@PathVariable("ids") String[] ids) {
        if (tagService.deleteTags(ids)) {
            return Result.ok();
        }
        return Result.error(500, "删除失败");
    }
}
