package run.stitch.blog.controller;

import cn.dev33.satoken.annotation.SaCheckRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;
import run.stitch.blog.dto.TagDTO;
import run.stitch.blog.service.TagService;
import run.stitch.blog.util.Result;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.List;

import static run.stitch.blog.util.StatusCode.FAIL;

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
    public Result saveTag(@RequestBody @NotNull TagDTO tagDTO) {
        Integer id = tagService.saveOrUpdateTag(tagDTO);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @PutMapping("/tags")
    public Result updateTag(@RequestBody @NotNull TagDTO tagDTO) {
        Integer id = tagService.saveOrUpdateTag(tagDTO);
        if (ObjectUtils.isEmpty(id)) {
            return Result.error(FAIL);
        }
        return Result.ok(new HashMap<>() {{
            put("id", id);
        }});
    }

    @SaCheckRole("ADMIN")
    @DeleteMapping("/tags/{ids}")
    public Result deleteTags(@PathVariable("ids") String[] ids) {
        if (tagService.deleteTags(ids)) {
            return Result.ok();
        }
        return Result.error(FAIL);
    }
}
